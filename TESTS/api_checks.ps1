# PowerShell API checks for PhoneShop webapp
$BASE = "http://localhost:8080/AssigmentPRJ301_Gr1-dev"
Write-Host "Base: $BASE"

Write-Host "1) GET supplier list"
$r = Invoke-WebRequest -Uri "$BASE/supplier" -UseBasicParsing -ErrorAction SilentlyContinue
if ($r -and $r.Content -match 'Supplier Management') { Write-Host 'OK: supplier list' } else { Write-Host 'WARN: supplier list not found' }

Write-Host "\n2) Add supplier (POST)"
$body = @{ action='add'; name='TestSupplierPS'; contact='PS'; phone='0901111222'; email='ps@test.local'; address='Hanoi'; logo='' }
Invoke-WebRequest -Uri "$BASE/supplier" -Method POST -Body $body -UseBasicParsing -ErrorAction SilentlyContinue | Select-Object -First 1

Write-Host "\n3) Check TestSupplierPS in list"
$r = Invoke-WebRequest -Uri "$BASE/supplier" -UseBasicParsing
if ($r.Content -match 'TestSupplierPS') { Write-Host 'OK: TestSupplierPS present' } else { Write-Host 'WARN: not found' }

Write-Host "\n4) Warranty lookup (POST)"
$imei = 'IMEI-S24-0001'
$r = Invoke-WebRequest -Uri "$BASE/warranty" -Method POST -Body @{ serial = $imei } -UseBasicParsing -ErrorAction SilentlyContinue
if ($r -and $r.Content -match 'Warranty') { Write-Host "OK: warranty data for $imei" } else { Write-Host "WARN: warranty not found for $imei" }

Write-Host "Done. Update BASE and IMEI as needed."