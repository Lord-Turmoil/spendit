Write-Host "Building project" -ForegroundColor Yellow
if (Test-Path dist) {
    Remove-Item -Recurse -Force dist
}
New-Item -ItemType Directory -Force -Path dist
Copy-Item index.html dist
Copy-Item logo.png dist
Write-Host "Build completed" -ForegroundColor Green