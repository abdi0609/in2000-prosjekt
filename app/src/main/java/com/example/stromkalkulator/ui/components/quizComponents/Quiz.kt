package com.example.stromkalkulator.ui.components.quizComponents

data class Question(val question: String, val alternatives: HashMap<String, String>)

data class Quiz(val questions: List<Question>, var answers: MutableList<String>,
                var finished: Boolean = false) {
    init {
        answers = MutableList(questions.size) {""}
    }
    fun answer(questionIndex: Int, answerString: String) {
        answers[questionIndex] = answerString
    }

    fun getTip(index: Int): String? {
        return questions[index].alternatives[answers[index]]
    }
}