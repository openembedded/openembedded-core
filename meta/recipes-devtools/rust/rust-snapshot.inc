## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.89.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "16ed8d8c7628a481c8501e7cd1022a123269b297bdedbb7f211f37a15e937e0e"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "abea0955dded88c68d731524ab9d29b162fae23bf5805b9f1dec063cba37c2aa"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "f9df3ee6d55a2387459b843477743fa386c3c0f126bd0be01691ee49309681b8"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "46512c61e217e613dc4d329fda8c6dde0d0ea94c4ae59c1ca2af9cf480458ff0"

SRC_URI[rustc-snapshot-i686.sha256sum] = "a5ca58ab1b419ddbf5e4ea9b206a3d1feb0cfc4e0879a96c2c2f809ee9ca00b0"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "853605815e8267baf8fdc6e4594514d2e0afb46fb29f478ebec871a3424430f3"
SRC_URI[cargo-snapshot-i686.sha256sum] = "1b3edcf590af9b9abdfa95f34b90a4258560627464a2a25d4e4503bb5834e291"
SRC_URI[clippy-snapshot-i686.sha256sum] = "2c47960ee78d804ce679b06c9c759e777ac7f39cae48bd8ffaf7ff706f951520"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "c090c2ab6b54a8d84129de4ef53bd4df35358191092d3217bf6fa99c20c860c6"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "2c5aa2cad944499c5b64947dcfeb12f3d5c91ced330a0982e000f22ec3dea27f"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "d22678ae984535464cd9aff3515e9ec5145ecb8db489ef4cce5c697e0538b500"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "0392c4fc97dc72b0d7d518129428c928b887e63b47f3d931fab7e54e2332ca9c"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "08de18310e7750796261b45d5faccc31ff94ff64148bb5d9fa65e9727a432010"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "a03b3c3facf4701b6c7e8f379f2aac445a582227b881e3268dee3752a84a89eb"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "ebdb0c8c9fad2d07dc702c9bfaee5dd140f6b0f9493bdd0c0d33f1449b7d97c2"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "f93cb4327943f30b3da8e671eab6a78cdc3110c6612ad2a5eeaa9d8597fbd452"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "6c66c8a3d698bb338d20453c88d768e87a5af8970af4f4082d18dec26c10da2c"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "29338a9b067fe1adfd4d5ad681577c2333b3ca266a631822f23fad0984dcca39"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "f23184532c1e2eef6df49358fdfb867e99e84db7c814ce667744a36bc2fd8d37"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "695228193ff41ac32ec562140294d87b1988311dfe685acc72b725429e2651d9"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "14b7a8b7b3951a59a765804e1dd2ad454d3412000e7166ad8f2f621e29a88941"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "40123b87f0b909376d7f0bd291553b7e10aa637dd710f4bc698bcdcd0b08d21b"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "764eb5f54c2b0779f9e84c6fb0763e45316d5a51fbd187b56f9f6d7a646272d4"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "682d112649355a637a871347d41317e361eb555bbfd4a4e48b26b0978449184b"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "2a2a00288faa8a55b16a3135c979bbd9c823f1e9ff376ea452ef0700ccc20d48"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "ee83276bb2d456116517638b8ff379c24ceffe1c0e381103739cb1db8e4c20da"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "ec9a8f609f23b196d09e6537dbaec7c6fd947e3b8266406c6654b2c112530de2"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "6bba0817c70b4dfae08238beb38003c623404d26b9ec00a297d08c9bf81c6d76"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "b2b811644d26d10195be0272b81bd28ed55aa998300c52b0d3b74bb40c162c42"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "2a7ed11535aac5b298a21799292ad8393b14a16acbac6358416406a0d2639746"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "92534c38ffe3a6b17964f37ca812c5d00f079011acb060ec036945d2f568a772"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "a4a050854353325c1710440a5f727e4280f1758fad5c0da6ba336ed57156fb48"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "b42c254e1349df86bd40bc28fdf386172a1a46f2eeabe3c7a08a75cf1fb60e27"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "2719470dcd78b3f97d78b978c8f85a1a58d84ff11b62558294621c01bca34d49"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "99fc10be2aeedf2c23a484f217bfa76458494495a0eee33e280d3616bb08282d"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "c6c362c6cd74567022e9ba0c16f6676f8c2b73d955adcf1f6f4c51cf15e57ce8"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "7909e4bff0a971286958f2a88c3bbf231d6ba4ac1add0b2aa3669adb9f869836"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "a23126912b494dc2fcad495f78f920e43c2b1f06159e45477664aa37c03d80fa"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
