package com.payamgr.wordchest.data.model

import com.payamgr.wordchest.data.util.StringUtil.extractWords

/**
 * Word structure
 * @property value            the word text
 * @property shortDescription a brief sentence showing the definition of the word
 */
data class Word(val value: String, val shortDescription: String) {
    /**
     * Detailed word definition structure
     * @param word          the word text
     * @param pronunciation the pronunciation of the [word]
     * @param sections      the different grammatical roles of the [word] divided in sections
     * @see Section
     */
    class Data(
        val word: String,
        val pronunciation: String,
        vararg val sections: Section,
    ) {
        /**
         * A single grammatical role of the word as a section
         * @property type        the grammatical role of the section + metadata
         * @property definitions the definitions of the word with this grammatical role
         * @see TypeWithMeta
         * @see Definition
         */
        class Section(
            val type: TypeWithMeta,
            vararg val definitions: Definition,
        ) {
            /**
             * constructor
             * @param type        the grammatical role of the section
             * @param definitions the definitions of the word with this grammatical role
             */
            constructor(type: Type, vararg definitions: Definition) : this(TypeWithMeta(type), *definitions)

            /**
             * The grammatical role of a section
             * @property label the label of the [type]
             */
            enum class Type(val label: String) {
                Noun("noun"),
                Verb("verb"),
                Adverb("adverb"),
                Adjective("adjective"),
                ;

                /**
                 * The plus operator is used to join the [type] with a [metaData]
                 * @see TypeWithMeta
                 */
                operator fun plus(metaData: String): TypeWithMeta = TypeWithMeta(this, metaData)
            }

            /**
             * A wrapper for [type] with [meta]
             * @property type the grammatical role of the section
             * @property meta the meta-data providing more information on the [type]
             * @see Type
             */
            data class TypeWithMeta(
                val type: Type,
                val meta: String? = null,
            )

            /**
             * A definition group of the words, that have related meanings
             * @property value   the main definition of the word at this group
             * @property samples other related definitions
             */
            class Definition(
                val value: Part,
                vararg val samples: Part,
            ) {
                /**
                 * A single unit of definition of the word
                 * @property subs a list of sub-parts used for styling purpose
                 */
                class Part(vararg val subs: Sub) {
                    /**
                     * A sub-part is used for styling purpose
                     * @property content       textual value of the sub-part
                     * @property style         the text style of the sub-part
                     * @property sentenceParts a list of substrings of the content,
                     *                         divided based if they are word or not
                     * @see Style
                     */
                    sealed class Sub(
                        val content: String,
                        val style: Style,
                    ) {
                        val sentenceParts = content.extractWords()

                        /** The text style types */
                        enum class Style { Normal, Sample, Meta }

                        /** Normal text style */
                        class Normal(content: String) : Sub(content, Style.Normal)
                        /** Sample text style */
                        class Sample(content: String) : Sub(content, Style.Sample)
                        /** Meta text style */
                        class Meta(content: String) : Sub(content, Style.Meta)
                    }
                }
            }
        }
    }
}
