package com.infosupport.ldoc.analyzerkt

import com.fasterxml.jackson.core.JsonGenerator
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.isTopLevelKtOrJavaMember

class AnalysisVisitor(gen: JsonGenerator) : KtVisitorVoid() {
    private val json = gen

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        if (classOrObject.isTopLevel()) {
            json.writeStartObject()
            json.writeStringField("FullName", classOrObject.fqName!!.asString())
            json.writeArrayFieldStart("Methods")
        }
        super.visitClassOrObject(classOrObject)
        if (classOrObject.isTopLevel()) {
            json.writeEndArray()
            json.writeEndObject()
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        if (!function.isTopLevelKtOrJavaMember()) {
            json.writeStartObject()
            json.writeStringField("Name", function.name)

            if (function.hasDeclaredReturnType()) {
                json.writeStringField("ReturnType", function.typeReference!!.text)
            }

            if (function.valueParameters.isNotEmpty()) {
                json.writeArrayFieldStart("Parameters")
                for (param in function.valueParameters) {
                    json.writeStartObject()
                    json.writeStringField("Type", param.typeReference!!.text)
                    json.writeStringField("Name", param.name)
                    json.writeEndObject()
                }
                json.writeEndArray()
            }

            json.writeArrayFieldStart("Statements")
        }
        super.visitNamedFunction(function)
        if (!function.isTopLevelKtOrJavaMember()) {
            json.writeEndArray()
            json.writeEndObject()
        }
    }

    override fun visitCallExpression(expression: KtCallExpression) {
        json.writeStartObject()

        json.writeStringField("\$type", "LivingDocumentation.InvocationDescription, LivingDocumentation.Descriptions")

        json.writeStringField("Name", expression.calleeExpression!!.text)

        if (expression.valueArguments.isNotEmpty()) {
            json.writeArrayFieldStart("Arguments")

            for (arg in expression.valueArguments) {
                json.writeStartObject()
                json.writeStringField("Type", "?")
                json.writeStringField("Text", arg.text)
                json.writeEndObject()
            }

            json.writeEndArray()
        }

        json.writeEndObject()

        super.visitCallExpression(expression)
    }

    override fun visitIfExpression(expression: KtIfExpression) {
        json.writeStartObject()
        json.writeStringField("\$type", "LivingDocumentation.If, LivingDocumentation.Statements")
        json.writeStringField("Condition", expression.condition!!.text)
        json.writeArrayFieldStart("Statements")
        super.visitIfExpression(expression)
        json.writeEndArray()
        json.writeEndObject()
    }

    override fun visitWhenExpression(expression: KtWhenExpression) {
        json.writeStartObject()
        json.writeStringField("\$type", "LivingDocumentation.Switch, LivingDocumentation.Statements")
        json.writeStringField("Expression", expression.subjectExpression!!.text)
        json.writeArrayFieldStart("Sections")
        super.visitWhenExpression(expression)
        json.writeEndArray()
        json.writeEndObject()
    }

    override fun visitWhenEntry(jetWhenEntry: KtWhenEntry) {
        json.writeStartObject()
        if (jetWhenEntry.conditions.isNotEmpty()) {
            json.writeArrayFieldStart("Labels")
            for (condition in jetWhenEntry.conditions) {
                json.writeString(condition.text)
            }
            json.writeEndArray()
        }
        json.writeArrayFieldStart("Statements")
        super.visitWhenEntry(jetWhenEntry)
        json.writeEndArray()
        json.writeEndObject()
    }

    override fun visitBinaryExpression(expression: KtBinaryExpression) {
        if (expression.operationReference.text == "=") {
            json.writeStartObject()
            json.writeStringField("\$type", "LivingDocumentation.AssignmentDescription, LivingDocumentation.Descriptions")
            json.writeStringField("Left", expression.left!!.text)
            json.writeStringField("Operator", expression.operationReference.text)
            json.writeStringField("Right", expression.right!!.text)
            json.writeEndObject()
        }
        super.visitBinaryExpression(expression)
    }

    override fun visitKtElement(element: KtElement) {
        element.acceptChildren(this)
        super.visitKtElement(element)
    }

    override fun visitKtFile(file: KtFile) {
        file.acceptChildren(this)
        super.visitKtFile(file)
    }
}
