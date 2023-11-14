package com.payamgr.wordchest.data.model

data class Word(val value: String, val shortDescription: String) {
    class Data(
        val word: String,
        val pronunciation: String,
        vararg val sections: Section,
    ) {
        class Section(
            val type: TypeWithMeta,
            vararg val definitions: Definition,
        ) {
            constructor(type: Type, vararg definitions: Definition) : this(TypeWithMeta(type), *definitions)

            enum class Type(val label: String) {
                Noun("noun"),
                Verb("verb"),
                Adverb("adverb"),
                Adjective("adjective"),
                ;

                operator fun plus(meta: String): TypeWithMeta = TypeWithMeta(this, meta)
            }

            data class TypeWithMeta(
                val type: Type,
                val meta: String? = null,
            )

            class Definition(
                val value: Part,
                vararg val samples: Part,
            ) {
                class Part(
                    vararg val subs: Sub,
                ) {
                    sealed class Sub(
                        val content: String,
                        val style: Style,
                    ) {
                        enum class Style {
                            Normal, Sample, Meta
                        }

                        class Normal(content: String) : Sub(content, Style.Normal)
                        class Sample(content: String) : Sub(content, Style.Sample)
                        class Meta(content: String) : Sub(content, Style.Meta)
                    }
                }
            }
        }
    }
}
