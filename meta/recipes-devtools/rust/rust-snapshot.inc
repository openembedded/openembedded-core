## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.96.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "76b1a6e8dd1636e364d4bbba685485ff44eee5ff6434add089bab4c703c7e19d"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "538e85452709687797d990579a491ff9b02f8bffba4a5d54cfa945e28868053e"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "09ea03e74aa94e07db7bc00bd2ec1ad86d90a7348c89fde3909a8922543b949f"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "f0304a59688cccc77da29bbfbb96cb29407b4fdc152337f8de198d81a01eac05"

SRC_URI[rustc-snapshot-i686.sha256sum] = "8caea1b6a1e76c9452cd2041c78680a9948b01abdc06be90c2e5ab3121793398"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "2d13b8c801c9ad1ff55dceacaf1d1777601e328bc56cd390c4209a2f61d76ce2"
SRC_URI[cargo-snapshot-i686.sha256sum] = "c478ba084e1f95d1dc0cc47d125ab415667277fbbed17d24b80c797cb3b71f42"
SRC_URI[clippy-snapshot-i686.sha256sum] = "f741c872aa729dbecc5bcf055306b82c3a28420a4ad1d0d0d3791b4e640b3eaf"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "173a4daefeae1b3a9f3888593acb64bc75b36e8a781056b1515d6ca30cac5fd9"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "1dcd3b58ceb6237d3e219d8e432260d4cac2a196210751c7f33ebe5537839c5f"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "80507b976a3698fd29f9d7db90bb1ba65b2a6f182d7f2f70dd44aa73fc30273a"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "3e6b7c5504eccb08d66adebaab3d93a0d88009317512c88404735cd4d1541f71"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "d3f4dc0ba3121ac18cb0948b63552573d4521cdf8e4504df916e2785a92d62d1"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "d434927909a92ec1298bd9ea51a3d251c1d5ef44c7ed334353cb832a7e124a7b"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "b905b1dd5c2f711f689ef0f641f0e1cc92d8977fbede3a31cdb299b7253b9560"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "e741b5bd87a5b7aca17d78a6a2e1cff49c3123710de6f3ade47a664359f6fd18"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "bf73e9dc666c3894a9edfe8afcc87f69f92c9eae3e401e35097e213f7e77e4d0"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "cf273029a7a8dba56fe4fd517ae4c8bd862a3e27c56369a8b9d6411c8d74085d"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "e9cbd8e65bf2a98662ff85d09e7059ef09ced38198961cc9433bda9b070fe53c"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "ccf270a4a2cd1bbebb1e8cbd4701057aeb8b552097e34c0103d6bcd4c78c998b"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "36f98b29bf631e27e0aaa2ffc1e33eb585a8f3a93e60e4b64e355b4e2aec4ede"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "347ff4e5dd463849f21391b9db90093dee33578d98f30b42bbb78b4777921747"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "6315038d99cb0270694afd72155faf899aebb700dd14c51f5d7186bfad73ab9a"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "725786e4c0362e1e406631aee93b340504dc452d82ead766ed5c15bda4099e32"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "4db1266212a226421893443d7ebdc679dc9bf04c9e68bc4140de50e0018c9463"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "595e4a980dccb7571441f330782cad6cd23d64f331ed453de57dd386665f5615"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "96917e09cb2494b6a26173b4bb81f8c485e3769771bfb6665e84591a7fee3561"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "ab5fc3717691786362095eac58493a29161fb809c5d60deba85fe6eb382b5c94"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "1b8c91ad1ba323c9155adc5a60c9bdc0b482357d8a052f72a5a8cd0bc0961474"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "92e5a9dcd09892d399963ab0e8de121e0027f2213168f32e13014641c0bfb13b"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "3d6ba391ed3a97b4cc33edaac6c793f4b979ff3fff5a45613cecbd1c26b4cd2b"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "3b44a8a94e1e9e43679f1bf3866408adc4b3e0b5402e8615d1341f10424490da"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "7d7fa1d0cfb0fab71a956bb78f41107202c17f30ab56c45288e869a37fd9633d"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "c09c7c646248f14f473f5f7a029af15ee57c3a9f9bc93dfa72d9621938586b82"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "dee75c3c8f9f600ad75bc0c93249e767d3047845a4dd668327ce43ab039ba266"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "9b0b89c67d5ce8195e1c8733587a63e326353fdda65f62455a64fa03e25659f2"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "3f42faca55ac0df4e94aa5ac3c6b86c4a4d5a623cfc084bb41b76674a7705d35"

SRC_URI[rust-std-snapshot-riscv64a23.sha256sum] = "727f9d546fe6b16d5b0eb4668cff217cef06266e28d1869c963db0597fad8086"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "687a80cee4631d02b820e7a652bd4e30661772a684fa0ce2f936cdd932f31d51"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
