## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.91.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "f3ea3c964b7f3b884337f2d411764032bbd1722d7f55592a547cbb29afd87c03"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "ff23dc81f796d64e34e866a44fd0bcae726e34014835369b8f9393a544167eca"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "003d7008219ca0d225ad1dfa301f7c079b123499430ee0780c85782e0878eeff"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "a5f8bd961fb30962144d2f68be0bdd1014cf07110323087b0d6a68251a7a4eca"

SRC_URI[rustc-snapshot-i686.sha256sum] = "bf718b184d6ace8fab0012835d129130a6dd6a8d78f4f742ae9308a86b96dfc5"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "059fe4b9802c3cb64b539cc501ff5e64f8d750573009c3004fd0b583b3f50d39"
SRC_URI[cargo-snapshot-i686.sha256sum] = "92486e31c070667681328ad57dd3bd4ca1e3d37958540d622b2afda9b67fbe3d"
SRC_URI[clippy-snapshot-i686.sha256sum] = "14cae2980accb8536ae700bb7ec6a3b540a06d84e7a4c71db1c7f05a17b1edbd"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "04bdbb954f48fcd0b125fbddbf3e59841ec81c58ff858471f7977c8343d9a73c"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "cb0c3c2704add4d1271713ff87e1b7a8a4f756b4dc9f580b62953532efd0f7b9"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "9437727091d30d73a987e2c56463a2de4f01fb69cfd229617e2f8a7d8de5c05a"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "060a0eb3ee236e8a3bfb2cad9495165d755ea60d32e62ce85f12ad17c26afedf"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "c625de10cbfd21031f71fa6fdd50ccaffd449744614ef1f57ee3cefe80c57792"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "d5ecd84b78eefaf30fb65ec169af1806bd06d0b5bbe166bf010b058c9c59d27f"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "1f8f0ac1b05acb7cc50592ca39ec78e374093a0701405e8ea1894bb64a7422cd"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "425e5a1a72cc20c476416682cb5df4222c0b9c28fbb9818d4d8f505ccea578e3"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "139a077f72197ce73031dae7cd2156fb6718b09fe9a3f4008266d34fb8d09ae6"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "ac046a72a957bf9fd1213e95d46d7ca431a6704f63e99c759ccdd9ae7c07f7d9"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "abc14cced999e0d5f0ec75f10af33998dcaef39fab27b8e2c63b93ca0b79f2ab"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "b8129667c5b30963698466b7edeef4c5a1c869e78b5f9218fcf1426defa5cea9"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "c526302dcf4f1627525b0eef3ef4181bea801f119c4a0963c3675b390cba164f"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "c79c071fd14ef094c1a2e750f19795e7e162f4237bf92299885c0d56fa6017fe"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "704bfa80ffeeafacb8f13043873e20e1b9c4a5e785ee6c5e7d0e5828fc1f0c8b"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "1571b18b39f97d808b9f10ed2697b9d87bc3959abf858bfcd3baf9eaf8d5d548"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "7c3d4ceaa630ddb45bcd538c687637a349b2c3e18634a2b944178a7b4b42a4cc"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "7486237c83844d48ac96b67241ef0c1f4f702ceb74c44721f6f8660c3068dede"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "8946ea952e5a516e8bb6fd859d9798902877e01bd4508b717f8b9fd915019ad5"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "97feb6d6b2158b410a83c40d7eeba1e97220e505eae6666aa09d112665cc8dc1"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "073c3817f91ff425ffa141a6db215d54b50db5a8abe08fed593b7597687bdf94"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "454ecc9a53dda63762f3d1d92b7e4f4459c58c89d39a4178cce1fd9fd0ce09b5"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "3af10597a739402d6ecb3db85abaf62e12f7b298d2df6377c3c80e09a54f91b6"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "8d3bb8aa79abcfe5c24a37651d09dd356f994663699e65e7dd9978514a5ebf31"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "a7169e8cb6174af2f45717703370363d8de82ce55f6ccba185893045b9370874"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "89e6520b16c12b43526440298d2da0dcb70747c5cc2d0b8e47d39b5da9aeef49"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "7103c03fb8abe85b23307005a9dfe4f01c826a89945d84b96fa2d03fd4d2d138"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "0087c3d58d2fdeafa89830c299b1026c9f981b49835db89c922b3c6a299b3225"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "47e91cba208b1f87348e7b6a49274ebb4364651a4191fa67e6850e7813e26ad5"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "4f7bffb09cf5a0c25dda9b51811c60996eb1ae06a277c1e836980c21b3a44766"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
