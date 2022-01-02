package org.jesperancinha.plugins.omni.reporter

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.SetEnvironmentVariable

internal class OmniReporterMojoTest {
    @Test
    @SetEnvironmentVariable.SetEnvironmentVariables(
        value = [SetEnvironmentVariable(key = "GITHUB_RUN_NUMBER", value = "BUILDO"),
            SetEnvironmentVariable(key = "GITHUB_JOB", value = "BUILDO")]
    )
    @Disabled
    fun `should startup with GitHub config`() {
        val gitHubRunNumber = System.getenv()["GITHUB_RUN_NUMBER"]
        val gitJobRunner = System.getenv()["GITHUB_JOB"]
        gitHubRunNumber.shouldBe("BUILDO")
        gitJobRunner.shouldBe("BUILDO")
        val omniReporterMojo = OmniReporterMojo()
        omniReporterMojo.shouldNotBeNull()
    }
}