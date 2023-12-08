## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.json
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.70.0"

SRC_URI[cargo-snapshot-aarch64.sha256sum] = "8fd2d9806f0601feab1485f79e46d1441af2158c68abf56788ff355d5c6b4ab5"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "966e85187b6b76dc520b23aadc886c5fe54b209a21c68f959ff00ef8542b7f9f"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "71698cf444eef74050db821dc4df996c0f268615230099cde836e685e5b5465d"

SRC_URI[cargo-snapshot-i686.sha256sum] = "5d20e9b8e56f641c3ef4aef965563806aa220044397485ec56f784657451a329"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "0e5543c35196ca5b08f1b7e49fa3f3b671444d75648504cdbcfd854e2e7efd2d"
SRC_URI[rustc-snapshot-i686.sha256sum] = "f89c90f80864f68e42b70d9c14984195629ed9894bb5cfe6c8a0f1140f7816ff"

SRC_URI[cargo-snapshot-mips.sha256sum] = "a272a2a34f03a89e1bc89d9bb11ec0dee08428f43b5103785a587091b3911c60"
SRC_URI[rust-std-snapshot-mips.sha256sum] = "0cd85a53e328b1c445a8ec13af87f74280c1d344744076b102227f626456caec"
SRC_URI[rustc-snapshot-mips.sha256sum] = "c1f51717bcec5a9c799d57d8c80eff7760c11fc5af383700fb22c3508dd9450e"

SRC_URI[cargo-snapshot-mipsel.sha256sum] = "25d2aaad531403359c60ed3035005db782ba041549fdb60d3ef1a5b5a04b2970"
SRC_URI[rust-std-snapshot-mipsel.sha256sum] = "58bf8a6407d92cd4786991f1cbdec7380001e07c057c3a8946c2e54593bb7f8c"
SRC_URI[rustc-snapshot-mipsel.sha256sum] = "0a6cf0eef9f1b67415ca5bbde1832cd3cd79560762251f3fb1b6f317ed5109bd"

SRC_URI[cargo-snapshot-powerpc.sha256sum] = "314ed27bbf7eed16f6ac0092b076a82782630c65927a241db921d1eff8b04759"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "101f358b08381b9c37c2f061bac1ff3e60c6036be20059c451f322a55f72376b"
SRC_URI[rustc-snapshot-powerpc.sha256sum] = "4e4fe3a8b2c0ea1f82e99a30c193b848464b7ac1ed802fddc100c11814a7b2ba"

SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "ccb5b4c03f4d359cbeb1739ba27b725b0fc9d0d55b22837b0d3904e778a7fdc9"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "468672f84e5b140269e188b836ec82494d65c18bda8b7cf1f9bf6fce19f222ab"
SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "7891593a85ba231ca91505720a3535e9cff22700ee1f3e122bc33a5f10ddf705"

SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "5f1751ab8d44a9c9e083d32a8039980d699a1224654e3f5aa0dfffbc4407f1c0"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "bbd882f9ec7f01a732ecc60cf50b7d8a10cd52c3de41c205f62938b6dc2b4dbc"
SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "7152b111cf91a2cfa1e7beb51d3c737561095ebdf75ec7e032ab1118639ea4ca"

SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "55c3a5c06b14c954df5318073c38a5b4b0ace3f9a32a24f0ef5514941f7c9502"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "cdecf32e245898306a27a0b164451b2ee9a2122176ce3a65f2ec73c200986dc3"
SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "7fa4de4b7f8ab8b23a0f2ef3dc4886658d3bf90a97f6b215146b75b445a6131b"

SRC_URI[cargo-snapshot-s390x.sha256sum] = "dc4104e9e4b82dff14ffb20233d412ff57cea27e7bb2064b6a7e660fee6d2c4d"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "7084afbe9fb1a0e8a0c3e760f45475ebba400b72030ae8359bdccef67cb48992"
SRC_URI[rustc-snapshot-s390x.sha256sum] = "2c8421d95d575d33545b969ed5d200bbcbc69a2ae80d0ffeba4c516fc8a91356"

SRC_URI[cargo-snapshot-x86_64.sha256sum] = "650e7a890a52869cd14e2305652bff775aec7fc2cf47fc62cf4a89ff07242333"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "0c0129717da1e27ccf2c56da950d2fe56973f71beec9e80ae6904b282d2f0ee9"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "7d891d3e9bc4f1151545c83cbe3bc6af9ed234388c45ca2e19641262f48615e2"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "f52e3ac35157e90f86e5ca6c177a43774425c24a51b15655ed61f6209c7db6f0"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "b06933f6f94cd30e1c2415b37db97a94533a727aa5d32a34ee80c6742ee418d2"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
