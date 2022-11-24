package com.varabyte.kobweb.gradle.core.utils

import org.jetbrains.kotlin.com.intellij.psi.PsiElement

fun PsiElement.visitAllChildren(visit: (PsiElement) -> Unit) {
    visit(this)
    children.forEach { it.visitAllChildren(visit) }
}