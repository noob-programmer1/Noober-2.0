package com.abhi165.noober.util


internal fun prettyPrint(jsonString: String): String {
    val indentation = "    "
    val builder = StringBuilder()
    var indentLevel = 0
    var inString = false

    for (char in jsonString) {
        when (char) {
            '{', '[' -> {
                builder.append(char)
                builder.append('\n')
                indentLevel++
                builder.append(indentation.repeat(indentLevel))
            }
            '}', ']' -> {
                builder.append('\n')
                indentLevel--
                builder.append(indentation.repeat(indentLevel))
                builder.append(char)
            }
            ',' -> {
                builder.append(char)
                if (!inString) {
                    builder.append('\n')
                    builder.append(indentation.repeat(indentLevel))
                }
            }
            '"' -> {
                builder.append(char)
                inString = !inString
            }
            else -> builder.append(char)
        }
    }

    return builder.toString()
}

fun generateCurlCommand(
    url: String,
    method: String = "GET",
    headers: Map<String, String> = emptyMap(),
    body: String? = null
): String {
    val curlCommand = StringBuilder("curl")

    // Add method (-X) option
    curlCommand.append(" -X $method")

    // Add headers (-H) options
    for ((key, value) in headers) {
        curlCommand.append(" -H \"$key: $value\"")
    }

    // Add the URL
    curlCommand.append(" \"$url\"")

    // Add request body if provided
    if (!body.isNullOrBlank()) {
        curlCommand.append(" -d \"$body\"")
    }

    return curlCommand.toString()
}

fun isURLValid(url: String): Boolean {
    val regex = Regex("""(http|https)://[a-zA-Z0-9.-]+(/[a-zA-Z0-9.-]+)?""")
    return regex.matches(url)
}