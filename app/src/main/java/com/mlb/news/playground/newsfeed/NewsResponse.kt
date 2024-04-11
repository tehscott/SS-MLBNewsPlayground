package com.mlb.news.playground.newsfeed

data class NewsResponse(
    val header: String,
    val link: Link,
    val articles: List<Article>,
)

data class Link(
    val language: String,
    val rel: List<String>,
    val href: String,
    val text: String,
    val shortText: String,
    val isExternal: Boolean,
    val isPremium: Boolean,
)

data class Article(
    val dataSourceIdentifier: String,
    val description: String,
    val type: String,
    val premium: Boolean,
    val links: Links?,
    val categories: List<Category>,
    val headline: String,
    val images: List<Image>,
    val published: String,
    val lastModified: String,
    val byline: String?,
)

data class Links(
    val api: Api?,
    val web: Web?,
    val mobile: Mobile?,
)

data class Api(
    val news: News,
    val self: Self,
)

data class News(
    val href: String,
)

data class Self(
    val href: String,
)

data class Web(
    val href: String,
)

data class Mobile(
    val href: String?,
)

data class Category(
    val id: Long?,
    val description: String?,
    val type: String,
    val sportId: Long?,
    val leagueId: Long?,
    val league: League?,
    val uid: String?,
    val createDate: String?,
    val teamId: Long?,
    val team: Team?,
    val athleteId: Long?,
    val athlete: Athlete?,
    val topicId: Long?,
    val guid: String?,
)

data class League(
    val id: Long,
    val description: String,
    val links: Links2,
)

data class Links2(
    val api: Api2,
    val web: Web2,
    val mobile: Mobile2,
)

data class Api2(
    val leagues: Leagues,
)

data class Leagues(
    val href: String,
)

data class Web2(
    val leagues: Leagues2,
)

data class Leagues2(
    val href: String,
)

data class Mobile2(
    val leagues: Leagues3,
)

data class Leagues3(
    val href: String,
)

data class Team(
    val id: Long,
    val description: String,
    val links: Links3,
)

data class Links3(
    val api: Api3,
    val web: Web3,
    val mobile: Mobile3,
)

data class Api3(
    val teams: Teams,
)

data class Teams(
    val href: String,
)

data class Web3(
    val teams: Teams2,
)

data class Teams2(
    val href: String,
)

data class Mobile3(
    val teams: Teams3,
)

data class Teams3(
    val href: String,
)

data class Athlete(
    val id: Long,
    val description: String,
    val links: Links4,
)

data class Links4(
    val api: Api4,
    val web: Web4,
    val mobile: Mobile4,
)

data class Api4(
    val athletes: Athletes,
)

data class Athletes(
    val href: String,
)

data class Web4(
    val athletes: Athletes2,
)

data class Athletes2(
    val href: String,
)

data class Mobile4(
    val athletes: Athletes3,
)

data class Athletes3(
    val href: String,
)

data class Image(
    val dataSourceIdentifier: String? = null,
    val name: String? = null,
    val width: Long? = null,
    val id: Long? = null,
    val credit: String? = null,
    val type: String? = null,
    val url: String? = null,
    val height: Long? = null,
    val alt: String? = null,
    val caption: String? = null,
)
