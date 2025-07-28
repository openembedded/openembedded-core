## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.87.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "93c59a880632aa1c69e3ffaa1830b5b19c08341ae2cd364bf4e6d13901facfed"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "80fab79c1f57b7cd89a1e6379b2196a208352403aa7bd7f674341a172ac0697f"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "51e237e7f383840a404a5be721491a8ca4671bf9c14e62566ecadccfcc6e4291"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "38b5bb919b5e256cc8bed5b69eecca577677ccb404fdd7bc3a9f2f79bede1a50"

SRC_URI[rustc-snapshot-i686.sha256sum] = "e59bb135589f8ca062e63ec244f61a4226fa348de5221fa96e9350f3e28a5fd1"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "df2bcaa75c9bfec12b4d0b43112c90647383865871cd0ea19f4af16b8942ac9f"
SRC_URI[cargo-snapshot-i686.sha256sum] = "840baaa8d99087b4fbb67c57cf0687f53b51f387b0c44ea206d5c6262114c9ed"
SRC_URI[clippy-snapshot-i686.sha256sum] = "fbaf630b50109512573152d66797760933a1aa44e4ab2056c39e2e5ef457ffd9"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "21f2e20d2dab04683f24771a4a1ba81500bf2d876b3f8e134b6f39ba88cafc33"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "333cf7f4328c13e2ea0d05fa87ee0509d63c57bab570b685c891fe6113e6d36a"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "ae613f51e848f09e2e2b079b0036f07beee965a3a15f80b39ec1f791c72c6a8f"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "7825d8bc9ab68f2015a1b432b26aad9f208b6293369a67887d2a2f8b5cabfb01"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "a650e3d8701c5916893845f5ceae1eb0bf9714ac608ef304976a9ee2ace16caf"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "df327566871a7595efc393e8fec9cd529615d4296727a228625cffd67a322126"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "b6ffefdf0c1bc00880aae0a2a4fc038e1412b7df364c8f601b63ca3589ef9e76"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "53307af7138bf26b8cd5da2057c2642830969b4482560c9fb1c229d984d8abdb"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "96c0a3d1a6f1c3d9ac677187af6837e1b0fb782a270762741d02b5c299c62d77"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "4a69e1a021ddd0c645fe7bee229a4db306c08a6f4976dc5eaf64bf95a810f7c2"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "c3992ae2b799941acd29561978115315ccc31fae851d23c7ac93c716b7b10ad7"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "7f968106335bb20b944ad0ff988ebe918b62a923d8f5fcfe8a998583ea22cebd"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "b861d2ccaa379261d7a05331b25971687a9d90efbab14c58829b78abe3add2e6"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "73ed9c6ef539095cdca4e41b789b4e47effce6f796be73542f86cb9d5855a9ea"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "2b9ef59c3c1818d6712ea4ec8af53720a0c2017ace80a15c287f05768ac04e0a"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "1dd6d1622f4e8fe33af90f4fca7851757fe2856248c36d79fdef38abc81f98b2"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "45708d5767ecd589ec852068dc0e0afb27427e7e9e03b4586f2cb75673f885b6"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "71f3a9dee3955255cd25ea4b15fe664284ac89591bc920667b9e47ff37f330fd"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "ced9e08041d4d24611593366537f3f14db63cede6a4d65c1297853b72c7b72d9"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "8d3665a04ecf3191196998f7aeb94cd6cdafba9200345387d854101629f94fe6"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "4b961ed0731f0f4f3c1270c1655198a01269e6e4ca129bf1b975114e3bde511e"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "9fb2a2ceb9671ab74ccceb61c5763c591eb1be21584b944ad7e5458edab171c9"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "73402a5585c32bdc80ace0070cec695768395fcc85efa0843ca42a453034a953"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "bab8cff53f93466c77d2f17b007132c77cf782314bfc6c3d897dc0fc1e4e6375"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "e8395c5c5756253b76107055e093ffbc4431af7b30aeebe72ce2684b9cb53973"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "1b57253bd32b8b292c965b3a2d992a266763158494cab8555584c09360b90f77"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "469d5dc479835adadd728bc3587f8abf1941b3dd71f9865abd3e0783ae662555"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "9d440eef6811112a000acf96a062df7bcbc013fa4f8e00a7f9a4f57ef90637a1"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "fc656140b5810a1bfa3b80456e416a0381dd68c565fc67fd23f945f8df80254b"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "2a7f654b1c31cb60b4841bf0a041a2fcdec9baecf8bb0f9ae1d1743862073907"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
