import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs
import java.util.*

class ProducerProperties {

    fun configureProperties() : Properties{

        val settings = Properties()
//        settings.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
        settings.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer")

        val api_key = "key"
        val api_secret = "secret"
        val broker = "abc.confluent.cloud:9092"


        settings.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "$broker")
        settings.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL")
        settings.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")

        settings.setProperty(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username='$api_key' password='$api_secret';")

        settings.setProperty("schema.registry.url", "https://abc.aws.confluent.cloud")
        settings.setProperty("basic.auth.credentials.source", "USER_INFO")
        settings.setProperty("schema.registry.basic.auth.user.info", "key:secret")

        // Required since we manually create schemas
        settings.setProperty("use.latest.version", "true")
        settings.setProperty("auto.register.schemas","false")

        // Required to produce the key to the DLQ
        settings.setProperty("key.serializer", "io.confluent.kafka.serializers.WrapperKeySerializer")
        settings.setProperty("wrapped.key.serializer", "org.apache.kafka.common.serialization.StringSerializer")

        return settings
    }
}