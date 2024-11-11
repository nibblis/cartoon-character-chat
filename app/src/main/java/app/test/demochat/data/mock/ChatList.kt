package app.test.demochat.data.mock

import app.test.demochat.data.model.ChatItem

fun getChats(): List<ChatItem> {
    return listOf(
        ChatItem(
            id = "cartman_1",
            title = "Eric Cartman",
            lastMessage = "Респект мой авторитет!",
            timestamp = 1699964400000, // 2023-11-14 12:00:00
            unreadCount = 3,
            avatarUrl = "https://upload.wikimedia.org/wikipedia/en/7/77/EricCartman.png"
        ),
        ChatItem(
            id = "peter_griffin_1",
            title = "Peter Griffin",
            lastMessage = "Хехехехехе, эй Лоис, смотри что я нашел!",
            timestamp = 1699962600000, // 2023-11-14 11:30:00
            unreadCount = 1,
            avatarUrl = "https://upload.wikimedia.org/wikipedia/en/c/c2/Peter_Griffin.png"
        ),
        ChatItem(
            id = "rick_sanchez_1",
            title = "Rick Sanchez",
            lastMessage = "Морти *burp* нам нужно отправиться в приключение!",
            timestamp = 1699960800000, // 2023-11-14 11:00:00
            unreadCount = 5,
            avatarUrl = "https://upload.wikimedia.org/wikipedia/ru/a/a6/Rick_Sanchez.png"
        ),
        ChatItem(
            id = "kenny_1",
            title = "Kenny McCormick",
            lastMessage = "Ммм мммм мм ммммм!",
            timestamp = 1699959000000, // 2023-11-14 10:30:00
            unreadCount = 0,
            avatarUrl = "https://upload.wikimedia.org/wikipedia/en/6/6f/KennyMcCormick.png"
        ),
        ChatItem(
            id = "stewie_1",
            title = "Stewie Griffin",
            lastMessage = "Что за дьявольщина творится, Брайан?",
            timestamp = 1699957200000, // 2023-11-14 10:00:00
            unreadCount = 2,
            avatarUrl = "https://upload.wikimedia.org/wikipedia/en/0/02/Stewie_Griffin.png"
        ),
        ChatItem(
            id = "morty_1",
            title = "Morty Smith",
            lastMessage = "Ох, я не знаю, Рик, это выглядит опасно...",
            timestamp = 1699955400000, // 2023-11-14 09:30:00
            unreadCount = 1,
            avatarUrl = "https://upload.wikimedia.org/wikipedia/en/c/c3/Morty_Smith.png"
        ),
        ChatItem(
            id = "woody_1",
            title = "Woody Johnson",
            lastMessage = "Боже, я люблю этот парк!",
            timestamp = 1699953600000, // 2023-11-14 09:00:00
            unreadCount = 4,
            avatarUrl = "https://static.wikia.nocookie.net/heroes-and-villain/images/5/53/Brickleberry.woody_johnson.jpg"
        ),
        ChatItem(
            id = "butters_1",
            title = "Butters Stotch",
            lastMessage = "О, гамбургеры!",
            timestamp = 1699951800000, // 2023-11-14 08:30:00
            unreadCount = 0,
            avatarUrl = "https://upload.wikimedia.org/wikipedia/en/0/06/ButtersStotch.png"
        ),
        ChatItem(
            id = "malloy_1",
            title = "Malloy",
            lastMessage = "Эй, толстяк, где мой мед?",
            timestamp = 1699950000000, // 2023-11-14 08:00:00
            unreadCount = 2,
            avatarUrl = "https://static.wikia.nocookie.net/brickleberry/images/2/28/Brickleberry_HD_CLEAR_Character_ART_Malloy.png"
        ),
        ChatItem(
            id = "brian_1",
            title = "Brian Griffin",
            lastMessage = "Стьюи, может хватит с этими изобретениями?",
            timestamp = 1699948200000, // 2023-11-14 07:30:00
            unreadCount = 1,
            avatarUrl = "https://upload.wikimedia.org/wikipedia/en/1/12/Brian_Griffin.png"
        )
    )
}