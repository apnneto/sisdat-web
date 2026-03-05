$ErrorActionPreference = "Stop"
$base    = "C:\Projetos\sisdat-web"
$staging = "$base\war-staging"
$warOut  = "$base\target\sisdat-web.war"

# ── 1. Clean and recreate staging folder ──────────────────────────────────────
Write-Host "==> Cleaning staging folder..."
if (Test-Path $staging) { Remove-Item $staging -Recurse -Force }
New-Item $staging -ItemType Directory | Out-Null

# ── 2. Copy all WebContent into staging root ──────────────────────────────────
Write-Host "==> Copying WebContent..."
Copy-Item "$base\WebContent\*" $staging -Recurse -Force

# ── 2b. Remove old EclipseLink JARs that conflict with Payara 5's built-in JPA ─
Write-Host "==> Removing conflicting EclipseLink JARs from staging..."
@("eclipselink.jar", "eclipselink-2.0.2.jar", "eclipselink-javax.persistence-2.0.jar") | ForEach-Object {
    $p = "$staging\WEB-INF\lib\$_"
    if (Test-Path $p) { Remove-Item $p -Force; Write-Host "  Removed: $_" }
}

# ── 3. Ensure WEB-INF/classes exists, then merge build/classes ────────────────
Write-Host "==> Merging compiled classes..."
New-Item "$staging\WEB-INF\classes" -ItemType Directory -Force | Out-Null
Copy-Item "$base\build\classes\*" "$staging\WEB-INF\classes" -Recurse -Force

# ── 4. Download Jersey 1.x jars into WEB-INF/lib if not already present ───────
$jerseyJars = @{
    "jersey-core-1.19.4.jar"    = "https://repo1.maven.org/maven2/com/sun/jersey/jersey-core/1.19.4/jersey-core-1.19.4.jar"
    "jersey-server-1.19.4.jar"  = "https://repo1.maven.org/maven2/com/sun/jersey/jersey-server/1.19.4/jersey-server-1.19.4.jar"
    "jersey-servlet-1.19.4.jar" = "https://repo1.maven.org/maven2/com/sun/jersey/jersey-servlet/1.19.4/jersey-servlet-1.19.4.jar"
    "jsr311-api-1.1.jar"        = "https://repo1.maven.org/maven2/javax/ws/rs/jsr311-api/1.1/jsr311-api-1.1.jar"
}
$libDir = "$staging\WEB-INF\lib"
New-Item $libDir -ItemType Directory -Force | Out-Null
foreach ($jar in $jerseyJars.Keys) {
    $dest = "$libDir\$jar"
    if (-not (Test-Path $dest)) {
        Write-Host "  Downloading $jar..."
        Invoke-WebRequest -UseBasicParsing -Uri $jerseyJars[$jar] -OutFile $dest
    } else {
        Write-Host "  $jar already present, skipping download."
    }
}

# ── 5. Verify key files exist ─────────────────────────────────────────────────
Write-Host "==> Verifying staging..."
@("index.jsp", "WEB-INF\web.xml", "WEB-INF\classes\com", "WEB-INF\lib\wicket-1.4.22.jar") | ForEach-Object {
    $p = "$staging\$_"
    if (Test-Path $p) { Write-Host "  OK: $_" }
    else               { Write-Host "  MISSING: $_"; throw "Missing required path: $p" }
}

# ── 6. Package the WAR ────────────────────────────────────────────────────────
Write-Host "==> Packaging WAR -> $warOut"
if (Test-Path $warOut) { Remove-Item $warOut -Force }
Push-Location $staging
& jar -cf $warOut .
Pop-Location

$size = (Get-Item $warOut).Length
Write-Host "==> WAR built: $warOut ($size bytes)"

# ── 7. Quick content check ────────────────────────────────────────────────────
Write-Host "==> Verifying WAR entries..."
$entries = & jar -tf $warOut
$checks = @("index.jsp", "WEB-INF/web.xml", "WEB-INF/classes/com/", "WEB-INF/lib/wicket-1.4.22.jar", "WEB-INF/lib/jersey-core-1.19.4.jar")
foreach ($c in $checks) {
    $found = $entries | Where-Object { $_ -like "*$c*" }
    if ($found) { Write-Host "  FOUND: $c" }
    else         { Write-Host "  NOT FOUND: $c" }
}
Write-Host "==> Done."
