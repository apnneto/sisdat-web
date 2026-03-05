$files = Get-ChildItem -Recurse -Filter *.java src
$count = 0
foreach($f in $files){
    $c = Get-Content $f.FullName -Raw
    $new = $c
    # Fix AbstractReadOnlyModel anonymous classes -> LambdaModel / Model.of
    $new = $new -replace 'new AbstractReadOnlyModel<String>\(\) \{\s*@Override\s*public String getObject\(\) \{\s*return \(item\.getIndex\(\) % 2 == 1\) \? "linha2" : "linha1";\s*\}\s*\}', 'org.apache.wicket.model.LambdaModel.of(() -> (item.getIndex() % 2 == 1) ? "linha2" : "linha1")'
    $new = $new -replace 'new AbstractReadOnlyModel<String>\(\) \{\s*@Override\s*public String getObject\(\) \{\s*return \(item\.getIndex\(\) % 2 == 1\) \? "linha1" : "linha2";\s*\}\s*\}', 'org.apache.wicket.model.LambdaModel.of(() -> (item.getIndex() % 2 == 1) ? "linha1" : "linha2")'
    # Fix AjaxSubmitLink onSubmit with Form -> without Form
    $new = $new -replace '(?:protected|public) void onSubmit\(AjaxRequestTarget (\w+), Form<\?>[^)]*\)', 'protected void onSubmit(AjaxRequestTarget $1)'
    # Fix RequestCycle.setRequestTarget for file download -> redirect
    $new = $new -replace 'RequestCycle\.get\(\)\.setRequestTarget\(\s*new ShowAnexoPage\(([^,]+),\s*([^)]+)\)\s*\)', 'setResponsePage(new org.apache.wicket.request.component.IRequestablePage(){}); // TODO: use file download resource'
    if($new -ne $c){ Set-Content $f.FullName $new -NoNewline; $count++ }
}
Write-Host "Updated $count files"
