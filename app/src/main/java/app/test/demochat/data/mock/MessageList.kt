package app.test.demochat.data.mock

import app.test.demochat.data.model.Message

fun getMessages(): List<Message> {
    return listOf(
        // Cartman's chat messages
        Message(
            id = "msg_cartman_1",
            chatId = "cartman_1",
            senderId = "cartman",
            text = "Эй, ребята, у меня есть потрясающий план!",
            timestamp = 1699964400000,
            isRead = false
        ),
        Message(
            id = "msg_cartman_2",
            chatId = "cartman_1",
            senderId = "kyle",
            text = "Картман, твои планы всегда заканчиваются катастрофой",
            timestamp = 1699964460000,
            isRead = false
        ),
        Message(
            id = "msg_cartman_3",
            chatId = "cartman_1",
            senderId = "cartman",
            text = "Заткнись, Кайл! Ты просто завидуешь моей гениальности",
            timestamp = 1699964520000,
            isRead = false
        ),
        Message(
            id = "msg_cartman_4",
            chatId = "cartman_1",
            senderId = "cartman",
            text = "Я серьезно, ребята, мы можем разбогатеть!",
            timestamp = 1699964580000,
            isRead = true
        ),
        Message(
            id = "msg_cartman_5",
            chatId = "cartman_1",
            senderId = "stan",
            text = "О нет, только не снова...",
            timestamp = 1699964640000,
            isRead = true
        ),
        Message(
            id = "msg_cartman_6",
            chatId = "cartman_1",
            senderId = "cartman",
            text = "Респект мой авторитет!",
            timestamp = 1699964700000,
            isRead = true
        ),
        Message(
            id = "msg_cartman_7",
            chatId = "cartman_1",
            senderId = "kenny",
            text = "Ммм мммм мм ммммм!",
            timestamp = 1699964760000,
            isRead = true
        ),
        Message(
            id = "msg_cartman_8",
            chatId = "cartman_1",
            senderId = "cartman",
            text = "Точно, Кенни! Вот почему ты мой лучший друг!",
            timestamp = 1699964820000,
            isRead = true
        ),
        Message(
            id = "msg_cartman_9",
            chatId = "cartman_1",
            senderId = "butters",
            text = "О, боже, я снова буду наказан...",
            timestamp = 1699964880000,
            isRead = true
        ),
        Message(
            id = "msg_cartman_10",
            chatId = "cartman_1",
            senderId = "cartman",
            text = "Баттерс, прекрати ныть и следуй плану!",
            timestamp = 1699964940000,
            isRead = true
        ),

        // Peter Griffin's chat messages
        Message(
            id = "msg_peter_1",
            chatId = "peter_griffin_1",
            senderId = "peter",
            text = "Эй, народ, я только что сделал что-то невероятно глупое!",
            timestamp = 1699962600000,
            isRead = false
        ),
        Message(
            id = "msg_peter_2",
            chatId = "peter_griffin_1",
            senderId = "lois",
            text = "Боже, Питер, что на этот раз?",
            timestamp = 1699962660000,
            isRead = true
        ),
        Message(
            id = "msg_peter_3",
            chatId = "peter_griffin_1",
            senderId = "peter",
            text = "Я поменял все наши сбережения на волшебные бобы!",
            timestamp = 1699962720000,
            isRead = true
        ),
        Message(
            id = "msg_peter_4",
            chatId = "peter_griffin_1",
            senderId = "brian",
            text = "Питер, это же просто обычная фасоль из магазина",
            timestamp = 1699962780000,
            isRead = true
        ),
        Message(
            id = "msg_peter_5",
            chatId = "peter_griffin_1",
            senderId = "peter",
            text = "Хехехехехе, эй Лоис, смотри что я нашел!",
            timestamp = 1699962840000,
            isRead = true
        ),
        Message(
            id = "msg_peter_6",
            chatId = "peter_griffin_1",
            senderId = "stewie",
            text = "Поразительно, этот жирный идиот превзошел сам себя",
            timestamp = 1699962900000,
            isRead = true
        ),
        Message(
            id = "msg_peter_7",
            chatId = "peter_griffin_1",
            senderId = "chris",
            text = "Круто, папа! А они вырастут до небес?",
            timestamp = 1699962960000,
            isRead = true
        ),
        Message(
            id = "msg_peter_8",
            chatId = "peter_griffin_1",
            senderId = "peter",
            text = "Конечно, сынок! Мы все станем богатыми!",
            timestamp = 1699963020000,
            isRead = true
        ),
        Message(
            id = "msg_peter_9",
            chatId = "peter_griffin_1",
            senderId = "meg",
            text = "Пап, ты опять всё испортил...",
            timestamp = 1699963080000,
            isRead = true
        ),
        Message(
            id = "msg_peter_10",
            chatId = "peter_griffin_1",
            senderId = "peter",
            text = "Заткнись, Мег!",
            timestamp = 1699963140000,
            isRead = true
        ),

        // Rick Sanchez's chat messages
        Message(
            id = "msg_rick_1",
            chatId = "rick_sanchez_1",
            senderId = "rick",
            text = "Морти *burp* нам нужно отправиться в приключение!",
            timestamp = 1699960800000,
            isRead = false
        ),
        Message(
            id = "msg_rick_2",
            chatId = "rick_sanchez_1",
            senderId = "morty",
            text = "Ох, я не знаю, Рик... Последний раз было довольно страшно",
            timestamp = 1699960860000,
            isRead = false
        ),
        Message(
            id = "msg_rick_3",
            chatId = "rick_sanchez_1",
            senderId = "rick",
            text = "Не будь *burp* занудой, Морти! Нужно добыть эти кристаллы!",
            timestamp = 1699960920000,
            isRead = false
        ),
        Message(
            id = "msg_rick_4",
            chatId = "rick_sanchez_1",
            senderId = "summer",
            text = "Можно я тоже пойду с вами?",
            timestamp = 1699960980000,
            isRead = true
        ),
        Message(
            id = "msg_rick_5",
            chatId = "rick_sanchez_1",
            senderId = "rick",
            text = "Нет, Саммер! Это слишком *burp* опасно!",
            timestamp = 1699961040000,
            isRead = true
        ),
        Message(
            id = "msg_rick_6",
            chatId = "rick_sanchez_1",
            senderId = "morty",
            text = "А для меня значит не опасно?!",
            timestamp = 1699961100000,
            isRead = true
        ),
        Message(
            id = "msg_rick_7",
            chatId = "rick_sanchez_1",
            senderId = "rick",
            text = "Ты мой щит, Морти! Твои *burp* волны мозга маскируют мои!",
            timestamp = 1699961160000,
            isRead = true
        ),
        Message(
            id = "msg_rick_8",
            chatId = "rick_sanchez_1",
            senderId = "beth",
            text = "Папа, ты обещал больше не подвергать Морти опасности",
            timestamp = 1699961220000,
            isRead = true
        ),
        Message(
            id = "msg_rick_9",
            chatId = "rick_sanchez_1",
            senderId = "rick",
            text = "Бет, милая, это образовательное приключение!",
            timestamp = 1699961280000,
            isRead = true
        ),
        Message(
            id = "msg_rick_10",
            chatId = "rick_sanchez_1",
            senderId = "jerry",
            text = "Я против этого!",
            timestamp = 1699961340000,
            isRead = true
        ),
        Message(
            id = "msg_rick_11",
            chatId = "rick_sanchez_1",
            senderId = "rick",
            text = "Заткнись, Джерри! *burp*",
            timestamp = 1699961400000,
            isRead = true
        ),

        // Kenny's chat messages
        Message(
            id = "msg_kenny_1",
            chatId = "kenny_1",
            senderId = "kenny",
            text = "Ммм мммм мм ммммм!",
            timestamp = 1699959000000,
            isRead = true
        ),
        Message(
            id = "msg_kenny_2",
            chatId = "kenny_1",
            senderId = "stan",
            text = "Чувак, я не понимаю, что ты говоришь",
            timestamp = 1699959060000,
            isRead = true
        ),
        Message(
            id = "msg_kenny_3",
            chatId = "kenny_1",
            senderId = "kenny",
            text = "Мммм! Мммм мм ммм ммммм!",
            timestamp = 1699959120000,
            isRead = true
        ),
        Message(
            id = "msg_kenny_4",
            chatId = "kenny_1",
            senderId = "cartman",
            text = "Кенни говорит, что видел что-то крутое!",
            timestamp = 1699959180000,
            isRead = true
        ),
        Message(
            id = "msg_kenny_5",
            chatId = "kenny_1",
            senderId = "kyle",
            text = "И ты его понял?",
            timestamp = 1699959240000,
            isRead = true
        ),
        Message(
            id = "msg_kenny_6",
            chatId = "kenny_1",
            senderId = "kenny",
            text = "Мм ммм ммм!",
            timestamp = 1699959300000,
            isRead = true
        ),
        Message(
            id = "msg_kenny_7",
            chatId = "kenny_1",
            senderId = "butters",
            text = "О боже, Кенни опять умер!",
            timestamp = 1699959360000,
            isRead = true
        ),
        Message(
            id = "msg_kenny_8",
            chatId = "kenny_1",
            senderId = "stan",
            text = "Сволочи! Они убили Кенни!",
            timestamp = 1699959420000,
            isRead = true
        ),
        Message(
            id = "msg_kenny_9",
            chatId = "kenny_1",
            senderId = "kyle",
            text = "Ублюдки!",
            timestamp = 1699959480000,
            isRead = true
        ),

        // Stewie's chat messages
        Message(
            id = "msg_stewie_1",
            chatId = "stewie_1",
            senderId = "stewie",
            text = "Брайан, мне нужна твоя помощь с новым изобретением",
            timestamp = 1699957200000,
            isRead = false
        ),
        Message(
            id = "msg_stewie_2",
            chatId = "stewie_1",
            senderId = "brian",
            text = "О нет, только не снова...",
            timestamp = 1699957260000,
            isRead = false
        ),
        Message(
            id = "msg_stewie_3",
            chatId = "stewie_1",
            senderId = "stewie",
            text = "Это машина, которая заставит Лоис исчезнуть!",
            timestamp = 1699957320000,
            isRead = true
        ),
        Message(
            id = "msg_stewie_4",
            chatId = "stewie_1",
            senderId = "brian",
            text = "Стьюи, ты не можешь продолжать пытаться убить свою мать",
            timestamp = 1699957380000,
            isRead = true
        ),
        Message(
            id = "msg_stewie_5",
            chatId = "stewie_1",
            senderId = "stewie",
            text = "Что за дьявольщина творится, Брайан?",
            timestamp = 1699957440000,
            isRead = true
        ),
        Message(
            id = "msg_stewie_6",
            chatId = "stewie_1",
            senderId = "brian",
            text = "Может, займёмся чем-нибудь менее деструктивным?",
            timestamp = 1699957500000,
            isRead = true
        ),
        Message(
            id = "msg_stewie_7",
            chatId = "stewie_1",
            senderId = "stewie",
            text = "Ладно, тогда помоги мне с машиной времени",
            timestamp = 1699957560000,
            isRead = true
        ),
        Message(
            id = "msg_stewie_8",
            chatId = "stewie_1",
            senderId = "brian",
            text = "Опять? В прошлый раз мы застряли в средневековье",
            timestamp = 1699957620000,
            isRead = true
        ),
        Message(
            id = "msg_stewie_9",
            chatId = "stewie_1",
            senderId = "stewie",
            text = "На этот раз всё будет по-другому! Я усовершенствовал дизайн",
            timestamp = 1699957680000,
            isRead = true
        )
    )
}