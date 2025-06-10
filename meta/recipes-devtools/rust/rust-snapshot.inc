## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.85.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "e742b768f67303010b002b515f6613c639e69ffcc78cd0857d6fe7989e9880f6"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "8af1d793f7820e9ad0ee23247a9123542c3ea23f8857a018651c7788af9bc5b7"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "cdebe48b066d512d664c13441e8fae2d0f67106c2080aa44289d98b24192b8bc"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "f3b8e769021a6bcee8174ac49a60c367effb6d97af0f2a6c44dc9ea39ff2bda9"

SRC_URI[rustc-snapshot-i686.sha256sum] = "c5a5751f3cfa027b53fee83d01dec8e555f3622b6993335aed12b1747aa9543c"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "401571adfe1223d83ed041e5a8c54c1f3afb3010d1d5b0c1fd79e7da1ab0a0de"
SRC_URI[cargo-snapshot-i686.sha256sum] = "3832027242911ab96fd86128587fc563596a5b10e8e38a073facf61c1ac9668f"
SRC_URI[clippy-snapshot-i686.sha256sum] = "74ed13a67c6ea0452c0d9e8697c6cfdd8280f983b207187ac93c287f72b65066"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "d43d2119ff25d879b2d280d3e5a03fcd21c57a32d479ad5626500c32432f18c9"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "2d05f5e13e7cb3ee6b0ca0dcd13a231a28eeb0faa4d15257a35cf7284149cd6a"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "9aa447eefcd3ba2f9dbab77596c8669849899ab243543fc8f1914089707081ac"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "d437b39465343a71ff475a65acb6c03bc0f800861f293f48accf1fa170458432"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "fbe7154abc641d6cacfdc6239ed4701fc7a09b5046c6d1bd209b59e7f2b2d0f0"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "ab2d5ac7cd295adaa4d959e8abbf42b372292c95d1a9f18aad64951ad6e2ab2b"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "e67c30a72f969c51ad199084b3ce0cdc573a4e21f732b2cc39dafbcb9807a5ce"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "497104d49803598cd3801749ac86cb26c88a2bd9c3a8c7bdc18fdf487f4650c0"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "f8e38291ba4241bc28e9c118d7a449ce3601abdeb2076f2f618858679fb3b929"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "e9ff89e759125ad2786ad2f6c40e820095720ea00fba66819d15fba79489430a"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "73cf850f6715acbf0029c7e5d4694825f61bd4e193e6413e65a78bcb13df97b7"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "29b63f23cd9af95846a246c9cc8349e514c3fb92ec0c840a38155e252d6cc668"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "371e40f9d6d82aecb056f70f82868d98ddc1c20510aa7d388442ac8d2d86a6ca"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "d0cfda4e18623d17922eb367e44ac9549ab3d5fe1c0bdbe0c2c95754255fa705"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "e1469f4249a7e21f872af487dcd1bd10ce388b5b3679bdab0cb0965e1e30fe47"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "d8c590bc2e6fc75800e41516427f5095ba0e15d7633167689499fcf6523dc61a"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "2da33044f17a8bc49a1478dc1eb2536df491a44d6e6bb0ec7c86f46877d44e98"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "0f5ecfe54859bbe715821f982b1a5396784fdf55605d00ece05d3e57a965ed16"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "c9f28b519799fc2d8089865e30fad04bb5339604d13725365fcd7ad7ee6c1489"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "9d21bc180196bccdb301e7e03034d3fd0df14b0c6520da0317bba7b1ce385fe8"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "308361737ed522aa77dd8b4ea91b5db244113ada5fa234d3ed008b526d07e2e8"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "d25053fbee1a0cb899bac8c8e4968aa35f9c3637e486cb5bb2c79ad43a78f3e7"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "7e1defd60095c91ded1612194f811ec5f2fc2e83bb914d35594d01fa3c54049d"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "9780d8c4cfef9d05460b3e540aad142391b2e3994accf8cfc8afb87fefac6cf0"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "7436f13797475082cd87aa65547449e01659d6a810b4cd5f8aedc48bb9f89dfb"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "285e105d25ebdf501341238d4c0594ecdda50ec9078f45095f793a736b1f1ac2"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "0aff33b57b0e0b102d762a2b53042846c1ca346cff4b7bd96b5c03c9e8e51d81"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "0dc3ca1708a6c9eed05d0802364377572b79ff079815b7bcbe8e3991f77c4117"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "3cb7318da10876de14076549d79410a8441923a92d62b616d8c105f5f5cd831b"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "65cd47af46a5b74e61ff6ae4cce8a979ee8670a57b8aa61e732828d1da073f73"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
