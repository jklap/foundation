package net.ntworld.foundation.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import net.ntworld.foundation.generator.setting.EventSettings
import net.ntworld.foundation.generator.type.ClassInfo
import net.ntworld.foundation.generator.type.EventField

object EventConverterGenerator {
    fun generate(settings: EventSettings): GeneratedFile {
        val target = Utility.findEventConverterTarget(settings)
        val file = buildFile(settings, target)
        val stringBuffer = StringBuffer()
        file.writeTo(stringBuffer)

        return Utility.buildGeneratedFile(target, stringBuffer.toString())
    }

    internal fun buildFile(settings: EventSettings, target: ClassInfo): FileSpec {
        val file = FileSpec.builder(target.packageName, target.className)
        Framework.addFileHeader(file, this::class.qualifiedName)
        file.addType(buildClass(settings, target))

        return file.build()
    }

    internal fun buildClass(settings: EventSettings, target: ClassInfo): TypeSpec {
        return TypeSpec.classBuilder(target.className)
            .addSuperinterface(
                Framework.EventConverter.parameterizedBy(
                    settings.event.toClassName()
                )
            )
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("infrastructure", Framework.Infrastructure)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("infrastructure", Framework.Infrastructure)
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("infrastructure")
                    .build()
            )
            .addProperty(
                PropertySpec.builder("json", Framework.Json)
                    .addModifiers(KModifier.PRIVATE)
                    .initializer("Json(%T.Stable)", Framework.JsonConfiguration)
                    .build()
            )
            .addType(buildCompanionObject(settings))
            .addFunction(buildToEventEntityFunction(settings))
            .addFunction(buildFromEventEntityFunction(settings))
            .build()
    }

    internal fun buildToEventEntityFunction(settings: EventSettings): FunSpec {
        val eventEntityTarget = Utility.findEventEntityTarget(settings)
        val code = CodeBlock.builder()

        code.add("val raw = json.stringify(%T.serializer(), event)\n", settings.implementation.toClassName())
        code.add(
            "val processed = %T.processRawJson(infrastructure, json, fields, raw)\n",
            Framework.EventEntityConverterUtility
        )
        code.add("return %T(\n", eventEntityTarget.toClassName())
        code.indent()
        code.add("id = infrastructure.root.idGeneratorOf(%T::class).generate(),\n", settings.event.toClassName())
        code.add("streamId = streamId,\n")
        code.add("streamType = streamType,\n")
        code.add("version = version,\n")
        code.add("data = processed.data,\n")
        code.add("metadata = processed.metadata\n")
        code.unindent()
        code.add(")\n")

        return FunSpec.builder("toEventEntity")
            .addModifiers(KModifier.OVERRIDE)
            .returns(eventEntityTarget.toClassName())
            .addParameter("streamId", String::class)
            .addParameter("streamType", String::class)
            .addParameter("version", Int::class)
            .addParameter("event", settings.event.toClassName())
            .addCode(code.build())
            .build()
    }

    internal fun buildFromEventEntityFunction(settings: EventSettings): FunSpec {
        val code = CodeBlock.builder()
        code.add(
            "val raw = %T.rebuildRawJson(infrastructure, json, fields, eventEntity.data, eventEntity.metadata)\n",
            Framework.EventEntityConverterUtility
        )
        code.add("return json.parse(%T.serializer(), raw)\n", settings.implementation.toClassName())

        return FunSpec.builder("fromEventEntity")
            .addModifiers(KModifier.OVERRIDE)
            .returns(ClassName(settings.event.packageName, settings.implementation.className))
            .addParameter("eventEntity", Framework.EventEntity)
            .addCode(code.build())
            .build()
    }

    internal fun buildCompanionObject(settings: EventSettings): TypeSpec {
        val code = CodeBlock.builder()
        code.add("\nmapOf(\n")
        code.indent()
        settings.fields.forEachIndexed { index, it ->
            code.add("%S to ", it.name)
            code.add(buildFieldSetting(it))

            if (index != settings.fields.lastIndex) {
                code.add(",")
            }
            code.add("\n")
        }
        code.unindent()
        code.add(")\n")

        return TypeSpec.companionObjectBuilder()
            .addProperty(
                PropertySpec.builder(
                    "fields",
                    ClassName("kotlin.collections", "Map").parameterizedBy(
                        ClassName("kotlin", "String"),
                        Framework.EventEntityConverterUtilitySetting
                    )
                )
                    .initializer(code.build())
                    .build()
            )
            .build()
    }

    internal fun buildFieldSetting(field: EventField): CodeBlock {
        if (field.metadata) {
            return CodeBlock.of("%T(metadata = true)", Framework.EventEntityConverterUtilitySetting)
        }

        if (field.encrypted) {
            return CodeBlock.of("%T(encrypted = true, faked = %S)", Framework.EventEntityConverterUtilitySetting, field.faked)
        }

        return CodeBlock.of("%T()", Framework.EventEntityConverterUtilitySetting)
    }
}