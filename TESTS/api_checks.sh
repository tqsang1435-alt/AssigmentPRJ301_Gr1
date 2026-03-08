#!/usr/bin/env bash
# API quick checks for the PhoneShop webapp
# Set BASE to your application base URL (adjust context path if needed)
BASE="http://localhost:8080/AssigmentPRJ301_Gr1-dev"

echo "Base URL: $BASE"

echo "1) GET supplier list"
curl -s "$BASE/supplier" | grep -i "Supplier Management" >/dev/null && echo "OK: supplier list returned" || echo "WARN: supplier list may not be reachable"

echo "\n2) Add supplier (POST)"
curl -s -X POST "$BASE/supplier" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "action=add&name=TestSupplier&contact=Tester&phone=0900000000&email=test@supplier.test&address=Hanoi&logo=" \
  -i | sed -n '1,5p'

echo "\n3) Fetch supplier list to confirm addition"
curl -s "$BASE/supplier" | grep -i "TestSupplier" >/dev/null && echo "OK: TestSupplier present" || echo "WARN: TestSupplier not found"

echo "\n4) Edit supplier (example: get edit form for id=1)"
curl -s "$BASE/supplier?action=edit&id=1" | sed -n '1,30p'

echo "\n5) Toggle supplier status (id=1)"
curl -s "$BASE/supplier?action=toggle&id=1" -I

echo "\n6) Delete supplier (hard delete) (id=1)"
curl -s "$BASE/supplier?action=remove&id=1" -I

echo "\n7) IMEI warranty lookup (sold IMEI example)"
# Replace IMEI string with the one in your DB (e.g. IMEI-S24-0001 from SQL seed)
IMEI_SAMPLE="IMEI-S24-0001"
curl -s -X POST "$BASE/warranty" -d "serial=$IMEI_SAMPLE" | grep -i "Warranty" >/dev/null && echo "OK: warranty info returned for $IMEI_SAMPLE" || echo "WARN: warranty info not found for $IMEI_SAMPLE"

echo "\nScript finished. Adjust BASE and IDs as needed."