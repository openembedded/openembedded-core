## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.json
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.81.0"

SRC_URI[cargo-snapshot-aarch64.sha256sum] = "76f8927e4923c26c51b60ef99a29f3609843b3a2730f0bdf2ea6958626f11b11"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "30a00260510403199d1cb919769b0a2e76eead15c352fc992bc193d795a2b2ff"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "85567f037cee338f8ec8f9b6287a7f200d221658a996cba254abc91606ece6f4"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "301f651f38f8c52ebaad0ac7eb211a5ea25c3b690686d1c265febeee62d2c6fc"

SRC_URI[cargo-snapshot-i686.sha256sum] = "44f74fbf64dd2627310e796cfcbde75c42c3435e93e880f1291c0e975b42c1f5"
SRC_URI[clippy-snapshot-i686.sha256sum] = "3d5cdbe24fd0cefe46bfa513dbf56631bef38d04bae1cfbebaa407c33430fecb"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "4ed9085460e444de9dee246080126f5e73062802b99aaff620d2aa827f60d972"
SRC_URI[rustc-snapshot-i686.sha256sum] = "8039f645445f99c0e293397b53a6696481b7d58166198605aca0eaa998f4f11f"

SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "d3a66e30a323fc20acd3b85f9a184b962a84ac9debf59c313d30c0146448cbb1"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "81044ebd81783b013cdc207a7304dc59baa7c1782a6f070ecfa1a6671844e26c"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "37f89523e04b960ba34c0cd145c4a78751961b6007ad4ae6d8b92389c488d696"
SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "90eb5646497f1cf566121b726b0598f76acf38ce9423b0889b4ad71dffb59aa1"

SRC_URI[cargo-snapshot-powerpc.sha256sum] = "3ffb73eaf288ebe02c06737c53398cbcf7f9e15bd53d6ec3f85be1364aff16ea"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "c6583a26dc5fda266c75b7bf2e5f6c1c9d5452f2dc3044bf99f43e59e14fead9"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "3b3473de46f7ea268130d8c72140dacab9118b2e8611fc0e23ed99091f25eb45"
SRC_URI[rustc-snapshot-powerpc.sha256sum] = "8da65f141f7b8c53d5802fc61711cccf28b0512a5f766809cbe882c6f8ec3011"

SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "687c665259646f859e6ef9b3b8baf49c2759e19a20aa029251130495fe5bb07d"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "ad77d9511489a7a95580d8c4062e7eddc509d5bdec590a074c7378f2e7f36b00"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "665f3c0a8752f8e5d973cf9b94e5c1be94954178ca8378a318b6e21e7a7b370c"
SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "c4e0968c6a16916a339a2dea9063d14f3847cee65534b525d2838b827e7dad18"

SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "813d2dcd603a1ad65a5de77515f4c94fdae301a1e1e8afcc2541076eebaba848"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "e35815af6cb90d70fd4730020ae48d4145487ff9b8c264caef5224c843d85744"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "5ba237cfbd18806bf77fbe8bc31b14a17f3d14acb30a022955cf047eb8d41056"
SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "734f407345b05617d62a30d96d8305b51b7cf7de3b1bdc160449726ea8f51ae0"

SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "4f41aeca96e6de516ad2150a98136948527907690301fef4f127676f165e159e"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "65a65485972507cb5e89e64056b4602489bae76cff4a9c152e69d91365ff7433"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "9882cda0a5547405e64357a4964d525f46d1395f85b7e7b6e69cbd4dbcd46ec3"
SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "05d16740639cc87d258fad152d2a0f7e74dc571b5216ae6260d645a2f4f09c84"

SRC_URI[cargo-snapshot-s390x.sha256sum] = "11291e98730186479854a304fccf586824e16f90b4e4cee6c9e17deaab04352a"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "38b9003148a5222a0fb117cae494bf2a05227e6eff877e26b233433809f81e01"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "5549622876714df21235aa6d26731f31c37a7e1629a3f6c5262dbb0b1f10038c"
SRC_URI[rustc-snapshot-s390x.sha256sum] = "6ea458b49aa9edc26f021cc48e6223d1cc05b1bf092312ada978e66037fa63e2"

SRC_URI[cargo-snapshot-x86_64.sha256sum] = "c50ee4b1ae8695461930e36d5465dddb7c7a0e0f0aa6cbd60de120b17c38b841"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "c545ea0f2901eb1cd652721350147df11744afbb97eb117d89b1f313e9920ffb"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "6ddf80f254e8eea9956308ba89fd68e1ac7885853df9239b07bbc9f047b7562f"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "988a4e4cdecebe4f4a0c52ec4ade5a5bfc58d6958969f5b1e8aac033bda2613e"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "91ebf62a1f95047b93d4a4fec280fb4897cc7921633fd55f5c5a3aeb2b140bd6"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "194a3c04a2390b1e07fdb114eb2c48e962219f0a1b710e2120a9806963a2520b"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"
RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
