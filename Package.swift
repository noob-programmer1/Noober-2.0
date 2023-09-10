// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/ABHI165/Noober-2.0/github/abhi165/noober/noober-kmmbridge/0.1-alpha2.1/noober-kmmbridge-0.1-alpha2.1.zip"
let remoteKotlinChecksum = "e8581eca0b33a497b89b5fb0cea3a20237f17c237f8ddea8f893099470cd0585"
let packageName = "Noober"
// END KMMBRIDGE BLOCK

let package = Package(
    name: packageName,
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: packageName,
            targets: [packageName]
        ),
    ],
    targets: [
        .binaryTarget(
            name: packageName,
            url: remoteKotlinUrl,
            checksum: remoteKotlinChecksum
        )
        ,
    ]
)