## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.88.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "b841d40bb98b2718c6452ec8421a4a8df584fce8d41875bcd9b1af83f52f7d96"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "e9ac4ff3c87247a2195fcceddbf1bdeee5c4fd337f014d8f4c4e3ac99002021f"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "5aa43865f2002914ce4fca8916b4403bfca62f17e779ad368f6a17553296a58b"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "e210ec3d6158bb4730dd168ae1d35d4f6f9e9bf629c8e14362dc45b10b404867"

SRC_URI[rustc-snapshot-i686.sha256sum] = "91feab28a2ba348840b15017303f43847974c75b443ea37b330ecb3d80c01007"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "ced56f7ab86496f6eb42d356841f9d0b625159eba65afd1913f232bb66d4b7e0"
SRC_URI[cargo-snapshot-i686.sha256sum] = "ebf94de491865963fc02a3cdbbf1fec8f3e9df49fe2af4d92c2a05cfddd5b3d8"
SRC_URI[clippy-snapshot-i686.sha256sum] = "aa2984ada59dc4e00f3820bb60dbde8d143a383b1c23f9133af6911bfb7d0ea5"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "6b00f9be938ce27a146d73a45fb7930c68b2bb87101d2af00c44541d8ed11ef9"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "f2bc8745ec01c7e560b3f7b214040fd8053be191f8839887b58b441992952e72"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "95499f3734dc2dab7de07bd502d98c10e3854aca1c0dfa6ad16c798ec6de8a36"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "176caa1f7048acb93c8136d2ef436d89da9374f85bd6686e8abc0dccd5242e90"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "17ef139220f60cbc6f54c6f85dce34cb2035a92ce321a1fbd1e76d340e54dbd2"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "891fe38736ca637bcd776f6d2b1822d761bd92305188482e45d26ab0dd84344b"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "ffd3d742fe3ffecf560b73c36ff6500ffc73b8899d58f98492d18f8fa71d7dcf"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "8889c94c8c0e27b34b93fefb598d312357ff1b4cc01a440d260bffb2c6e5c191"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "e1755dd3552209d564b77d20f5267d824851bfba2ecfb4bcb768deeea45078bc"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "89cf8b30e0fae601e0603e41d5eaf58881729208cac83636eff89b94724b7112"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "d234100429586cf8a3e7dd74149f95a864a4bd2ed914db72c13edcc4b4accd2f"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "bbbcdab98f901429766b89efd6d30934f8d35c489be909c95635295f180d0cf5"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "4acf67a5352df836246f457ae61ce40feb38a3360aec13881f5a26f22323ccb4"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "1030e71f755692c3429bb16829cca776c1ac372abbce3001b04d4e46b1e4f32f"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "f7a561ad6b0d6ccf5d6c24336fe96e215cda7215f538baaf6f0f5c7fea6030d0"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "a5b851a61427f4d85229fd3b588794e34b62b9862e577de8d110ac13abbfb9c2"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "f46ff6b6707e1e48519bf18c5dede1730cce6b2e347f4f15d841f919df92b859"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "d446fc0d8e4ce985c2d21aa2adb6ec9fce8f7ebde85655200abd71e142b9a981"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "38bca9242e9468335e433ac2231f2e778c351b3ac200de059be4789360a3956e"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "f467dfe7bdab310ac88efbf4ada1bf823982e0a3aff13f8ef35e33a6246d886f"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "f214cb9a93428ebf24b9f9261b6833c53a91738f6e98029cf7bedf023caaf42c"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "df3144f87a22d606abea4dcfe88369fcd499ef2fc748724b47941308ae259a5d"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "6d3e6072f556f9ac11425bb273a2982e3255435d803994a51201709601c5f2d5"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "fb5ecad2896871c5db00cbaad11a039a5c071ba8d748ea1ce87df7dfa804bfe1"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "b049fd57fce274d10013e2cf0e05f215f68f6580865abc52178f66ae9bf43fd8"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "36d7eacf46bd5199cb433e49a9ed9c9b380d82f8a0ebc05e89b43b51c070c955"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "856962610ee821648cee32e3d6abac667af7bb7ea6ec6f3d184cc31e66044f6b"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "db09c9e4a8a0b486781d87403f74a203a58d9ef0a58ba10c39264916d93ac603"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "7c9016ce85e24dde9d76c0a34061fa0fd6f742ea495dd1784804b60061b39627"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "95be6d0dec8be07fa13313a10dd177e72df9737660c235a3e673bffcdd2e9068"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
