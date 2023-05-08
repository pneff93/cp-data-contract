# CC Data Contract - Data Quality Rule

[Data Contract Documentation](https://docs.confluent.io/platform/current/schema-registry/fundamentals/data-contracts.html)

# Register Schema with Rules

We register a schema with a defined rule:

```shell
curl --request POST --url 'https://abc.confluent.cloud/subjects/sensor-data-raw-value/versions'   \
  --header 'Authorization: Basic xyz' \
  --header 'content-type: application/octet-stream' \
  --data '{
            "schemaType": "AVRO",
            "schema": "{\"type\":\"record\",\"name\":\"SensorData\",\"namespace\":\"com.kafkaStreamsExample\",\"fields\":[{\"name\":\"sensorId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"type\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"value\",\"type\":\"float\"},{\"name\":\"unit\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"timestamp\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"]}]}",
            "metadata": {
            "properties": {
            "owner": "Patrick Neff",
            "email": "pneff@confluent.io"
            }
        },
        "ruleSet": {
        "domainRules": [
            {
            "name": "checkSensor",
            "kind": "CONDITION",
            "type": "CEL",
            "mode": "WRITE",
            "expr": "message.sensorId == \"sensor_1\"",
            "params": {
            "dlq.topic": "sensor-data-raw-dlq"
            },
            "onFailure": "DLQ"
            }
            ]
        }
    }' 
```

## Check Schemas
```shell
curl --request GET \
  --url 'https://abc.confluent.cloud/subjects/sensor-data-raw-value/versions/latest' \
  --header 'Authorization: Basic xyz' \ | jq
```

## Run

Ensure to create the topics `sensor-data-raw` and `sensor-data-raw-dlq` in CC in advance.

```
./gradlew run
```
The producer produces events continuously.
The first 5 records pass the rule and are sent to the sensor-data-raw topic. The second 4 records fail and are sent to the dlq-topic.

The SR in CC is able to work with Data Contracts but the UI is not able to display it.
