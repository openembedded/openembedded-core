## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.json
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.71.0"

SRC_URI[cargo-snapshot-aarch64.sha256sum] = "13e8ff23d6af976a45f3ab451bf698e318a8d1823d588ff8a989555096f894a8"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "58542a0ab1162ce05a45eb751793782dc24c5bf8eb9a7467317f254260305ea6"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "e61b6e34df8c3a002798a9f627c4da701d66f9fc066a70264e354b03d06e6722"

SRC_URI[cargo-snapshot-i686.sha256sum] = "ee836c079c53e8e2ed5b2cbad849f991f2142b0b47b593a29b5cb39a76ee910e"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "46976bf8297efc65556ccddfec395d5327fbe303f9dabffd104628a37cb1de6d"
SRC_URI[rustc-snapshot-i686.sha256sum] = "e7c9c943b727de4f978cc9366e5c42536992d52ad418ffb9015e836723fd75ca"

SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "33dd282f63a2ccf0251baec5fa8d989276b1735528fe4b5153319038cedbe115"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "1a7c473cb50248a505a0e08dc05df49291e48a302a7b138e3ce396b0d0df9dbb"
SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "fbb21fefc8dee4c73f151b15f175caaabf10c3e122974d3fea3f3e2fbe916822"

SRC_URI[cargo-snapshot-mips.sha256sum] = "ad068f683e44dbf539ab28d9a1ad1508d22b932aaa81ab0f77df2237817ef1e8"
SRC_URI[rust-std-snapshot-mips.sha256sum] = "a259bddfc3ee36279b016ebd2682fc5b4d1b93c63bed6e4f57b5a2963d661dc9"
SRC_URI[rustc-snapshot-mips.sha256sum] = "10d663c3b6cbf1d2166c2d544a2567c83bd8fb110c80064263393b89f025204c"

SRC_URI[cargo-snapshot-mipsel.sha256sum] = "c7973b800cadb67f6c5a4b938e03bb9e891ca5ab153da10a7913d9c3e18c9371"
SRC_URI[rust-std-snapshot-mipsel.sha256sum] = "ad91d662c93e5092367048c8e717f64a5096b876623e5fbc3c791b2d1f868dfb"
SRC_URI[rustc-snapshot-mipsel.sha256sum] = "05d15f51fc33439582967dfc8f19364e67e8b5050abefbdd8913984e8a7be84e"

SRC_URI[cargo-snapshot-powerpc.sha256sum] = "38d0a67429bbbe5e56ba30110c77d8dab3ebe96f8159de1d973da74e5bcbbc71"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "fac6cc57d8a922423db2f0239b8484df3b029d0b58a63676868de682680bbf87"
SRC_URI[rustc-snapshot-powerpc.sha256sum] = "1961433bbe12e84f2a01daa713779cb4a3fc53b856447047efee3ad02e596164"

SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "a8f7d55c8e17db9737fad863b0b9327d79acf2baad2159d099b6d79e40ae79a5"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "628957fbe1f8665578167c6814486941f7b04417dce8a499cababa3284a2260a"
SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "0b14c621f0144d454fbe6093a31171e91163ea2a566092f35364817f5c77a96b"

SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "9e6e184ad1836f52e5863b6ed72619926bb2e8c7ccf2b3c96bf86d7783f19673"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "3ce73e032232522cd7c37e64b1e8895f16ddcef02b1646d078f67bb36b6c1494"
SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "9c4e3d5740e48265ab59888090013793777a526ecf5541c0fb055f9f0a13f409"

SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "bb78d8d09c8bb34d47a11276bc5566565a1dd075e1a5f4388347c767ba2b06a2"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "bf692cee46d6c04c3993795633fdc5d4c1dffa7e4d4924227fa3e044872440a4"
SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "1941967619e0a3b6b8d6c3175bd5530c8b1bf30a7044c071f1b1808a56532ea7"

SRC_URI[cargo-snapshot-s390x.sha256sum] = "6ab6597d7e42e7a94246ec6679b6a5479e95ca84e76cc952544514ff901da605"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "4ceab97a7c15e1235aa099306798e1eb8620aad5477b7123dc9e343bb9a6fcfe"
SRC_URI[rustc-snapshot-s390x.sha256sum] = "b94e7db76f3a0b13b8bc6f51f65f3a66ac3694ab2cf63b03c3f56ba6e7cce841"

SRC_URI[cargo-snapshot-x86_64.sha256sum] = "fe6fb520f59966300ee661d18b37c36cb3e614877c4c01dfedf987b8a9c577e9"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "98ae6530c3a41167e9d93d11ea078be98a02f6d809a06d0d51af3ce0f73150d7"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "c293d906769671d1cd18e945671bbd14e0b8a41df5075c47f33e6086fc8a1558"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "5878f641f4e5a12ced79d2f0d5d9a9d5b0b97b56967684cf09357162c3e6a7b7"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "679ef3ee4a3876f071e08539734de2fabfcc3410977e8c20ba685bc9f40a693b"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
