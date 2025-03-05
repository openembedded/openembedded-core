## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.json
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.82.0"

SRC_URI[cargo-snapshot-aarch64.sha256sum] = "05c0d904a82cddb8a00b0bbdd276ad7e24dea62a7b6c380413ab1e5a4ed70a56"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "1e01808028b67a49f57925ea72b8a2155fbec346cd694d951577c63312ba9217"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "1359ac1f3a123ae5da0ee9e47b98bb9e799578eefd9f347ff9bafd57a1d74a7f"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "2958e667202819f6ba1ea88a2a36d7b6a49aad7e460b79ebbb5cf9221b96f599"

SRC_URI[cargo-snapshot-i686.sha256sum] = "1cf5bae93e9724f50f1a20a752f05870565e0ce83baa157aad632aa162fd97d5"
SRC_URI[clippy-snapshot-i686.sha256sum] = "a4c20f20c8e53747e00948f90d74380ae890c1836a067d504eca540c6a60157f"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "c7ebb02b6ca349999b3b7dce768efaca5e0d9060f55397523e11a64cd10a9405"
SRC_URI[rustc-snapshot-i686.sha256sum] = "92bab38ce88b7738fb62a1bccf496b1b2975fe984cc4fc506903a03ed37e1e4a"

SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "01709e6d5b16e972e6c5e826a36331c645766ecda8f0aa48559f20c0d1129f44"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "ddbd18682a7fa0794d3bf159ba5cc5aee8c7bd6860d2edd8c9f4591753b5f05c"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "8cc1399b0460b3bc4b7295165dcdcee310fd643dc29569e1786944650f2a3700"
SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "2bdbf08c8c7d3a4dc8bddddbc4cf2cdec5a3d0f4a42e19183a14bed24d8d580c"

SRC_URI[cargo-snapshot-powerpc.sha256sum] = "7c11f69eb52c6e26cd8a33daeae7d630b6af49de30c1af16f4e998ca25a48bfd"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "a33f459a5cc13573e4ed73bd5667bba4bdf658e30b629f36a221eb5249d4acc6"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "827989a42526206199025f764d08d7cd319fee58bf2e08dcaf75df9ead6f094e"
SRC_URI[rustc-snapshot-powerpc.sha256sum] = "0d93e17a06cd284bdc0a97df64733f4b2f9f8c3cd9163935d85812ebc8520d2f"

SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "94d828f3cbd848f15ec72e904bccd904a7fac0cd88d6afecfc472e48da032cb2"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "c849be4677f42dc9439ac2458ca3964b69a2d6621e57f979151e7b59de22d895"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "665f3c0a8752f8e5d973cf9b94e5c1be94954178ca8378a318b6e21e7a7b370c"
SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "8795c642f94d15c81420a8eff77efbbac553869a00c627a1e3f881c542388b4a"

SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "ce5918e9c1a41b9e9a1b3e15f6525ddc01df06d9cea63d574dc59e8a401c1b67"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "a633c961cc9225c5ec1af6f153e33fd103057c9ebc66eb80f388687f34215fbf"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "4954a44305622b8f68ca088138b9a69432e350336da1a415a282ce148beb340d"
SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "0b22b37e51dd1dab6cc80a02968873ecb5f886fe1a65cd47131e3d10be48a23b"

SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "4779732566542099491c35a9f4342361bb8366e76c7d37ade0b5b7ae6f470df3"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "1399034c20e3037288cd659359e0a9eeb1d8a125488bbea28bbe146792a18d1a"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "5649f9b4e107d6c5f72fb7f82cfe15370b80b8d983670602e02e0b615bc6dc88"
SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "225726a3fedeb6cf136d5af503fb6853a0ce3c735fd972d40d678d734e65f5e8"

SRC_URI[cargo-snapshot-s390x.sha256sum] = "55d95ce07117430b92d1153e5b1ff40222687bd344872260e0a19ecf7b4c28a2"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "9c8c4b9d3a39383aa043a9a534478bd2ab6adf7a2a48c1d4d29864974f23005f"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "454b83668b57bdef28fc49aca8fd4c38e395b49011e653afdf3f9bb2dae8aa63"
SRC_URI[rustc-snapshot-s390x.sha256sum] = "bd46920849468c4ab78c53b21c962e538b1e8a9f38c736a9e52d790c75d55c45"

SRC_URI[cargo-snapshot-x86_64.sha256sum] = "97aeae783874a932c4500f4d36473297945edf6294d63871784217d608718e70"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "ea4fbf6fbd3686d4f6e2a77953e2d42a86ea31e49a5f79ec038762c413b15577"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "2eca3d36f7928f877c334909f35fe202fbcecce109ccf3b439284c2cb7849594"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "90b61494f5ccfd4d1ca9a5ce4a0af49a253ca435c701d9c44e3e44b5faf70cb8"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "f56585c55664898c7484f4b7bd139770c99d7b4da6e56e4016f71cb053b1aee2"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "87db52d782131a8817b76f65eefcae2c24a49b2a6f19ed9bd4699167305c22aa"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"
RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
