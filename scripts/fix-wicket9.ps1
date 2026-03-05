$files = Get-ChildItem -Recurse -Filter *.java src
$count = 0
foreach($f in $files){
    $c = Get-Content $f.FullName -Raw
    $new = $c
    $new = $new -replace '(?:protected|public) void onError\(AjaxRequestTarget (\w+)[^)]*Form[^)]*\)', 'public void onError(AjaxRequestTarget $1)'
    $new = $new -replace '(?:protected|public) void onSubmit\((?:final )?AjaxRequestTarget (\w+)[^)]*Form[^)]*\)', 'protected void onSubmit(AjaxRequestTarget $1)'
    if($new -ne $c){ Set-Content $f.FullName $new -NoNewline; $count++ }
}
Write-Host "Updated $count files"
