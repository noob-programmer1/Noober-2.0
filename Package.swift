// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/ABHI165/Noober-2.0/io/github/abhi165/noober-kmmbridge/0.1/noober-kmmbridge-0.1.zip"
let remoteKotlinChecksum = "9c558aa9fec0ae9f727c0fe8b4e0434bd27256b46209ed3d0f3fe72e921c8514"
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