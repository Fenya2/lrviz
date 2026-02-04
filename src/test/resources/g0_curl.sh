curl -X POST http://localhost:8080/api/build/lr0 \
  -H "Content-Type: application/json" \
    -d '{
  "terminals" : [ "(", ")" ],
  "nonTerminals" : [ "S" ],
  "rules" : [ {
    "left" : "S",
    "right" : "(S)"
  }, {
    "left" : "S",
    "right" : ""
  } ],
  "startSymbol" : "S"
  }' | jq
