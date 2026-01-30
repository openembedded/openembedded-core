## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.92.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "7c8706fad4c038b5eacab0092e15db54d2b365d5f3323ca046fe987f814e7826"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "ce2ab42c09d633b0a8b4b65a297c700ae0fad47aae890f75894782f95be7e36d"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "cb2ce6be6411b986e25c71ad8a813f9dfbe3461738136fd684e3644f8dd75df4"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "333ab38c673b589468b8293b525e5704fb52515d9d516ee28d3d34dd5a63d3c3"

SRC_URI[rustc-snapshot-i686.sha256sum] = "c1c920cd59e255e5dcf844af22b80483fe4b04ff04f143be6da69314bd6733f8"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "abc840631a4462a4c8ec61341110ff653ab2ef86ef3b10f84489d00cc8a9310d"
SRC_URI[cargo-snapshot-i686.sha256sum] = "63001bbde1c036fae9ad624807bfb18117c7e7cb4a04de81adc4acc7e8580c07"
SRC_URI[clippy-snapshot-i686.sha256sum] = "f21090ddb01503ab7e326ff8b1ab44121611121e83572aa2dba2c5ceffe12d22"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "6207ea209b332c01437fe742c2ef2bc914c88b6d4f9a23a43bc01ad924f6e4a8"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "62e2568ebf6f1addc750a8c32dd1fa4fef8d27679cbac33b837afeb54f204819"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "32f4b393f1471d7d50c9a50512f02116187b3ea6a885c26ff2d74f87fa72baa8"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "70783e0b31682938db53b78f87c948acfe589e3ece5dee6ae6aa5b667a384f3c"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "961cb6b8c2b5e969ad8eb0256fee43b88608b27310d946a7df3c9e9577139cae"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "c3e809a324b00eb53096c58df38645bb496c6560de334dfe04ed0b77c0605aaa"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "1249c64f95d8187b3a89cf8429329bdbc98f53a9fc538e898c95c487e3234d90"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "93ed9fdd6fc2655c7789ed096d57a47178532b6bda1ec4480adca7920563e934"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "357dd4745f978080d35dbb6192ecfea331abbb93d88be7d70407856b30722a40"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "2ce706afa4a46b6773340854de877fc63618a40e351298a4e3da8eb482619863"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "e94a8f97c3143158e285b4f8ade1fae844961caca30c0eeed44f6c14da484f05"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "7912584254f455b90ed6ce6089c4efded33bb753d1bf948286ef2f4f782d2f30"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "f48a214ff6fa36a77de8a0a2ee009be8aa48ba1533792450aca5edac29497606"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "eba59766c2d9805c0a1fc82fd723acbb36569e1bec1088c037bba84d965f70ba"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "49dd01f5d10e3f368181223db0fbf7dc191161f7d80a295de4909bf87e53da58"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "873047278043e6506f5c98b8f633a9b96dc49ff83869a86a151fe5b1b9455044"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "729f4c074ccda30bff493fa94f255bcfcec1be594fecd8a4d0f57da845121c64"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "8ee20dcf3b1063fa6069b3ce85e1fcf42794dfa783263314865cb53fff42d9e4"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "e8708d0a929ad15941ecde368e8a156911927a805b492afafc737124b6aa55d4"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "bdedabeedd48080e6ef70376c52338fbb45ff80d1fbb12a81aa2d8cabbb706c8"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "6a8cf614a74f7469c70d32b378f0163633e6bbe19624b248b758501c5bcdfe84"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "ebf944dc95015498d322504a54e4f9cdb28590f7790aa3a9eb86d6cf4b6c93ff"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "a290edf359a5632dfda3b2325a309d9edeb58c97de97e3cc67274335b58adf05"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "41aa44f1cf37cac87b83e450eb19148c539ff266bb40dc146207ad5b9e1e1acc"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "78b2dd9c6b1fcd2621fa81c611cf5e2d6950690775038b585c64f364422886e0"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "5f106805ed86ebf8df287039e53a45cf974391ef4d088c2760776b05b8e48b5d"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "e5e12be2c7126a7036c8adf573078a28b92611f5767cc9bd0a6f7c83081df103"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "2c1bf6e7da8ec50feba03fe188fc9a744ba59e2c6ece7970c13e201d08defa9a"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "8b984b31ffca1f27d32ee77ec2cc4c2ab59e72beab67bfce13bbd926dbea8715"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "d85afb14120c3c7367338a565a920db653dccd4bc5062398791d7b62b89fd1fd"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
