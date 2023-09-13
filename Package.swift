// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/ABHI165/Noober-2.0/io/github/abhi165/noober-kmmbridge/0.1/noober-kmmbridge-0.1.zip"
let remoteKotlinChecksum = "f35de0b43e671281d86d92a33ebe3dd94cf0e36b9fb67514a1bb0f1a0dda54ed"
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