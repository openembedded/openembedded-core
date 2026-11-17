## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.99.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "89c0f1a3a44df63c95e5d5f2ba093d0a12c5f5f57f8635f8a7a0ce50cf86600e"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "1cf7e2ef58ed1cfa6f1adea18e155cbf254c1951f39e5f5e3376879f55fd64a4"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "113229fcc16cc1ba004923a4cc275fbf3281dafaaf8b97269f9c00189d497fd4"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "42e49dc02d29949395c0c072585961f6c9b5408110236087828d4e2f94ca96ef"

SRC_URI[rustc-snapshot-i686.sha256sum] = "17d94ac00b3a5ae896022aabd9e73fb56f01881c9bd3d5095e08d9c9e608f4ac"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "b13902a225a6109588de09167831da111c70a30bedb2d9b9754d9fb13dc6e558"
SRC_URI[cargo-snapshot-i686.sha256sum] = "a6cdcfd06454695375a3aa89c37b0431b5146d55b1111fc323ac92a15d1d087a"
SRC_URI[clippy-snapshot-i686.sha256sum] = "e3295d8f8fc7658e8135a3fdad3a9f7d7dc34d38540f4efc720502dcf952fc86"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "70e0b964f64c462ebb0b9ef1c4e08c3a87a724fbae16b1ca866127e68d4f5e7d"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "31418c9492c57eff5893f7d80d6cf776d968dfcfe1750be8c2bf2c05b19689d9"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "dd5901c5668513c0640fff626aa7fce42d33ceb18afecc91e1163063ea859cd1"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "ef24b5ed352469eb5efd9eed4e68928ff684682865a70c5e74c372b5d356f034"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "ad672e3d515cfbb33e8e05d4fb15a5dfa745988b9699f5b62d1cb9ba13dde81c"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "f2ce1424fc9272a64f358f8d28c72f277f7fa626c256f115daae0ecfa12a8999"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "a511dd7f315fe6097a12de73c1b4041ae6b5c2880e88ed2afba2b36d79074bf7"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "bfc3191f0086878a7e72b3362f8a36ccd9bb9a88ad14c122334443b16eb39f28"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "fd891495f63d0aa1168875f8a0160ec1587d2ef6bb83979f03e9aa7c21ca8362"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "8b9ac17c3410a1ca895c2280c839fd1d1df7adcb6435137eb3030bb255ee69cf"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "18134aecb63ee24aa980c9843cf24bdaeb9c2dd8aeca832ae5b88ca5d8856981"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "388e0748e40ec77ab799806e1f48b615ab8e65220d3c9a6838dfa172bea8b4a0"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "308c82f4c4ba93e7614a27660347ed6b419f3570c5bc1046f73d840b7728dbc9"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "8c1a6857ba1be316ff453efc4d2aefd43756ecdb6d549a3d64be7351c4a55bdd"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "bbdf0aa56ce2dd7347e9a06861f9bd2d8015e2dadcf80edcb843892efc68174a"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "4ce616ff452ec0491876d00fe1c89fab786b6ce7fd0289c0e0f47da1bfac7039"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "80db2efdcf26ba75b1c38c6da896ff332d1bb6d0fbde6b1d2336a7ea475c6e40"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "646847eb1d0b58c55118390a2b1a6af871e4bc581651b5047dcbdfd0a24629f9"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "999311c0ee828b3988f16abcd586d1189ebab6d37081197d89a9883c4b7d2134"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "c4ae4e5e03e3ce8c2f2b93a4b59e2f3c8f47a1647de29458f8e26e76079b0d61"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "b598d8f89ac024fa9839de2393787eb85bed071528236f0a282e8100d852cb4a"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "ec386455256a3ef1d16bccb02a040cbb6978af565f716fdc26684054b19dd0d0"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "c6f4d75baf64f08a449d89d1f5cacb4e14a3be40a8623a00ea1530a6e3fa9d9e"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "ad97b001f4c73442307434176fc97cdcf7a80820d728f48bebfb31237ca8b55c"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "77171ba2a0345fdf2abc4fedda55d6de078dae7a68527c28be8c77dcc9604bd5"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "3e58dff2d0b72196b5ea4e90536e174d400de88564a52694686b81e091169933"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "d7674918d28093097614cd9728b6ca60db9ea3038f640f0bd1e9a4188c7568ce"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "982442e32ad8dd3f0bb3a30db74038da5f71fd9bf1a865cc71da8ea5d4ec71ec"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "0d2e0687934fd8f79e75a092e9a4c1240ca8346b61aa22dae0921a3d66274b18"

SRC_URI[rust-std-snapshot-riscv64a23.sha256sum] = "e63bf434820d6a2f95396428d1659dac8ef915bc1432d730091ae399518d5fbe"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "ef4399ed88f527bb71b937583a7e5bf36177d83fb55b8c58973ae146579c5ed6"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
