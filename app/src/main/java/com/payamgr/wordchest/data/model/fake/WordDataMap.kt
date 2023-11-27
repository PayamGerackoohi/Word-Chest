package com.payamgr.wordchest.data.model.fake

import com.payamgr.wordchest.data.model.Word.*
import com.payamgr.wordchest.data.model.Word.Data.*
import com.payamgr.wordchest.data.model.Word.Data.Section.*
import com.payamgr.wordchest.data.model.Word.Data.Section.Type.*
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition.*
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition.Part.Sub.*

/**
 * In-memory data feed for demonastration, separated for lighter code analysis burden
 */
@Suppress("SpellCheckingInspection")
object WordDataMap {
    fun build(): Map<String, List<Data>> = mapOf(
        "sample" to listOf(
            Data(
                "sample",
                "ˈsæmpəl",
                Section(
                    Noun,
                    Definition(
                        Part(
                            Normal("A small part or quantity intended to show what the whole is like: "),
                            Sample("investigations involved analyzing samples of handwriting."),
                        ),
                        Part(
                            Normal("a specimen taken for scientific testing or analysis: "),
                            Sample("a urine sample."),
                        ),
                        Part(
                            Meta("Statistics "),
                            Normal("a portion drawn from a population, the study of which is intended to lead to statistical estimates of the attributes of the whole population."),
                        ),
                        Part(
                            Normal("a small amount of a food or other commodity, especially one given to a prospective customer."),
                        ),
                        Part(
                            Normal("a sound created by sampling."),
                        ),
                    ),
                ),
                Section(
                    Verb + "[with object]",
                    Definition(
                        Part(
                            Normal("take a sample or samples of (something) for analysis: "),
                            Sample("bone marrow cells were sampled "),
                            Normal("| (as adjective, with submodifier sampled) : "),
                            Sample("a survey of two hundred randomly sampled households."),
                        ),
                        Part(
                            Normal("try the qualities of (food or drink) by tasting it."),
                        ),
                        Part(
                            Normal("get a representative experience of: "),
                            Sample("sample the pleasures of Saint Maarten."),
                        ),
                        Part(
                            Meta("Electronics "),
                            Normal("ascertain the momentary value of (an analog signal) many times a second so as to convert the signal to digital form."),
                        ),
                        Part(
                            Normal("record or extract a small piece of music or sound digitally for reuse as part of a composition or song."),
                        ),
                    ),
                ),
            ),
        ),
        "sound" to listOf(
            Data(
                "sound",
                "saʊnd",
                Section(
                    Noun,
                    Definition(
                        Part(
                            Normal("Vibrations that travel through the air or another medium and can be heard when they reach a person's or animal's ear: "),
                            Sample("light travels faster than sound."),
                        ),
                        Part(
                            Normal("a thing that can be heard: "),
                            Sample("she heard the sound of voices in the hall | don't make a sound."),
                        ),
                        Part(
                            Normal("the area or distance within which something can be heard: "),
                            Sample("we were always within sound of the train whistles."),
                        )
                    ),
                    Definition(
                        Part(
                            Normal("(also musical sound) sound produced by continuous and regular vibrations, as opposed to noise."),
                        ),
                    ),
                    Definition(
                        Part(
                            Normal("music, speech, and sound effects when recorded, used to accompany a film or video production, or broadcast: "),
                            Meta("[as modifier] : "),
                            Sample("a sound studio."),
                        ),
                        Part(
                            Normal("broadcasting by radio as distinct from television."),
                        ),
                        Part(
                            Normal("the distinctive quality of the music of a particular composer or performer or of the sound produced by a particular musical instrument: "),
                            Sample("the sound of the Beatles."),
                        ),
                        Part(
                            Normal("(sounds) "),
                            Meta("informal "),
                            Normal("music, especially popular music: "),
                            Sample("sounds of the Sixties."),
                        ),
                    ),
                    Definition(
                        Part(
                            Normal("the ideas or impressions conveyed by words: "),
                            Sample("you've had a hard day, by the sound of it."),
                        ),
                    ),
                ),
                Section(
                    Verb,
                    Definition(
                        Part(
                            Normal("emit or cause to emit sound: "),
                            Meta("[no object] : "),
                            Sample("a loud buzzer sounded "),
                            Normal("| "),
                            Meta("[with object] : "),
                            Sample("she sounded the horn."),
                        ),
                        Part(
                            Meta("[with object] "),
                            Normal("give an audible signal to indicate (something): "),
                            Sample("a different bell begins to sound midnight."),
                        ),
                        Part(
                            Meta("[with object] "),
                            Normal("express or convey (a warning): "),
                            Sample("pharmaceutical companies are sounding the alarm about counterfeit drugs."),
                        ),
                        Part(
                            Meta("[with object] "),
                            Normal("test (the lungs or another body cavity) by noting the sound they produce: "),
                            Sample("the doctor sounded her chest."),
                        ),
                    ),
                    Definition(
                        Part(
                            Meta("[no object] "),
                            Normal("convey a specified impression when heard: "),
                            Meta("[with complement] : "),
                            Sample("he sounded worried."),
                        ),
                        Part(
                            Normal("(of something or someone that has been described to one) convey a specified impression: "),
                            Sample("it sounds as though you really do believe that "),
                            Normal("| "),
                            Meta("[with complement] : "),
                            Sample("the house sounds lovely."),
                        ),
                    ),
                ),
            ),
            Data(
                "sound",
                "saʊnd",
                Section(
                    Adjective,
                    Definition(
                        Part(
                            Normal("In good condition; not damaged, injured, or diseased: "),
                            Sample("they returned safe and sound "),
                            Normal("| "),
                            Sample("he was not of sound mind."),
                        ),
                        Part(
                            Normal("financially secure: "),
                            Sample("she could get her business on a sound footing for the first time."),
                        ),
                    ),
                    Definition(
                        Part(
                            Normal("based on reason, sense, or judgment: "),
                            Sample("sound advice for healthy living "),
                            Normal("| "),
                            Sample("the scientific content is sound."),
                        ),
                        Part(
                            Normal("competent, reliable, or holding acceptable views: "),
                            Sample("he's a bit stuffy, but he's very sound on his law."),
                        ),
                    ),
                    Definition(
                        Part(
                            Normal("(of sleep) deep and undisturbed: "),
                            Sample("a doze that deepened into a sound sleep."),
                        ),
                        Part(
                            Normal("(of a person) tending to sleep deeply: "),
                            Sample("I am a sound sleeper."),
                        ),
                    ),
                    Definition(
                        Part(
                            Normal("severe: "),
                            Sample("such people should be given a sound thrashing."),
                        ),
                    ),
                ),
                Section(
                    Adverb,
                    Definition(
                        Part(
                            Normal("soundly: "),
                            Sample("he was sound asleep."),
                        ),
                    ),
                ),
            ),
            Data(
                "sound",
                "saʊnd",
                Section(
                    Verb,
                    Definition(
                        Part(
                            Meta("[with object] "),
                            Normal("Ascertain (the depth of water), typically by means of a line or pole or using sound echoes: "),
                            Sample("Mr. Pattison was sounding the depth of the water with a pole."),
                        ),
                        Part(
                            Meta("Medicine "),
                            Normal("examine (a person's bladder or other internal cavity) with a long surgical probe."),
                        ),
                    ),
                    Definition(
                        Part(
                            Meta("[with object] "),
                            Normal("question (someone), typically in a cautious or discreet way, as to their opinions or feelings on a subject: "),
                            Sample("we'll sound out our representatives first."),
                        ),
                        Part(
                            Normal("inquire into (someone's opinions of feelings) in a cautious way: "),
                            Sample("officials arrived to sound out public opinion at meetings in factories."),
                        ),
                    ),
                    Definition(
                        Part(
                            Meta("[no object] "),
                            Normal("(especially of a whale) dive down steeply to a great depth: "),
                            Sample("he sounded, arching his back steeply and raising his rubbery flukes in the air."),
                        ),
                    ),
                ),
                Section(
                    Noun,
                    Definition(
                        Part(
                            Normal("a long surgical probe, typically with a curved, blunt end."),
                        ),
                    ),
                ),
            ),
            Data(
                "sound",
                "saʊnd",
                Section(
                    Noun,
                    Definition(
                        Part(
                            Normal("A narrow stretch of water forming an inlet or connecting two wider areas of water such as two seas or a sea and a lake: "),
                            Sample("a small rowboat came from the south on its way into the sound."),
                        ),
                        Part(
                            Normal("(the Sound) another name for Øresund: "),
                            Sample("the new project will link the two nations at the narrowest part of the Sound."),
                        ),
                    ),
                ),
            ),
        ),
        "pneumonoultramicroscopicsilicovolcanoconiosis" to listOf(
            Data(
                "pneumonoultramicroscopicsilicovolcanoconiosis",
                "nuˌmɑnoʊˌəltrəˌmaɪkrəˌskɑpɪkˌsɪləkoʊvɑlˌkeɪnoʊˌkoʊniˈoʊsəs, ˌnumənoʊˌəltrəˌmaɪkrəˌskɑpɪkˌsɪləkoʊvɑlˌkeɪnoʊˌkoʊniˈoʊsəs, nuˌmoʊnoʊˌəltrəˌmaɪkrəˌskɑpɪkˌsɪləkoʊvɑlˌkeɪnoʊˌkoʊniˈoʊsəs",
                Section(
                    Noun,
                    Definition(
                        Part(
                            Normal("An invented long word said to mean a lung disease caused by inhaling very fine ash and sand dust.")
                        ),
                    ),
                ),
            ),
        ),
    )
}
