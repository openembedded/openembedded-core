## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.txt
## TODO: find a way to add additional SRC_URIs based on the contents of an
##       earlier SRC_URI.
RS_VERSION = "1.56.1"
CARGO_VERSION = "1.56.1"

# TODO: Add hashes for other architecture toolchains as well. Make a script?
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "b01011cbb5503c456ecc6a557a38e099994b8497df545c661ce8fd48c5beadc6"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "a7001d1218b62d377cab15522d1b1c376b073c05f7d0ff32cf278871a5eeda3d"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "dfed65a50e2b58b6807c1fb6f8afa7abd5c3b22c682d505721d615823687c708"

SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "a83416d15354e4dfa1c1e4a756282c6be7169679f2b04eca82ed34e2116b93f0"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "77aec6a8c5f3d33941c79a48cda3bb08878c23dd1947dc027dfe5c4da41305b3"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "3d263eb1871b5d6ca4b198b9611925923e9353e1f5c2becf8c7b784298e88743"

SRC_URI += " \
    https://static.rust-lang.org/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${BUILD_ARCH};subdir=rust-snapshot-components \
    https://static.rust-lang.org/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${BUILD_ARCH};subdir=rust-snapshot-components \
    https://static.rust-lang.org/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_STD_SNAPSHOT = "rust-std-${RS_VERSION}-${BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${RS_VERSION}-${BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${CARGO_VERSION}-${BUILD_ARCH}-unknown-linux-gnu"
