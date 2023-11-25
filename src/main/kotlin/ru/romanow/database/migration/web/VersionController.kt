package ru.romanow.database.migration.web

import org.springframework.boot.info.GitProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.romanow.database.migration.models.VersionResponse

@RestController
@RequestMapping("/api/v1/version")
class VersionController(
    private val gitProperties: GitProperties
) {

    @GetMapping
    fun version(): VersionResponse {
        return VersionResponse(
            version = gitProperties.get("build.version"),
            commit = gitProperties.shortCommitId,
            time = gitProperties.get("commit.time")
        )
    }
}
