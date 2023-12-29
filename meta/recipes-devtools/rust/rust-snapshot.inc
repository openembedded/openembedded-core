## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.json
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.72.0"

SRC_URI[cargo-snapshot-aarch64.sha256sum] = "95741a4cd2073adbd74a7c5596bb912abf4b2dfe00d70a9919cba4a836b7a0ff"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "41d259c6f84280fd0e7719fea03a7583ba54e33e8ac32a2a7b703ffb0aebb7d9"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "1948a80453956d494457dcced1942e2e204fb26d4e57e718ef1c7aa378efbedb"

SRC_URI[cargo-snapshot-i686.sha256sum] = "549eda5cda44750b0b2e6d3ce3f9c90c3a133b695e4882b4c6b93e54d6e8a73a"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "536c5ec1403b55045a502af1d6f8af192b560fbf9a24874bce6d59163fb8a38a"
SRC_URI[rustc-snapshot-i686.sha256sum] = "53c0e2045078326fd2ac9e77900a34b4ced1545a489b2a438deaebd2150cf543"

SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "216a9ccbfab10778f977b62416c17cf94a374e84a7acf3f50a11cf0b8b88940c"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "116065f0bb6d8e13725b974f057709e12ed85d93217a0feb16581850db7a0ede"
SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "73633f52ff12c448d2523409967d8d5d1e0b664c5a3a214395d10a9be7cc020d"

SRC_URI[cargo-snapshot-powerpc.sha256sum] = "8a6452262e062be0e7eef92b2aafaa06caf0853b264a3fd337e92079a43f0a24"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "cca2897f091227cc53d63f3eefcfcb65f42d9e24a04a1b37a0d3ee36b7f84b5b"
SRC_URI[rustc-snapshot-powerpc.sha256sum] = "3a8f0f115ae2d599e1de12d38cb47bd7f9b508e213bf2f9a41efc136021c49a2"

SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "a24a385d9f403e0800adb4d8364e663e40a04663a18df8d8704a5156d4232a1b"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "4aa7b7e80a5c45825f01aee96a88c5bcd56317c66298c4bd4ce99c80095e492e"
SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "f1863c033fa88ebb9628f38988da54fadd16075b06765dc93abcd8b4f58f557b"

SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "f659bf3ab70c376c736b7d7112d1fcee32a56dbfa66f6ef4fc039652f66c99e7"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "b6ef684ebf77063dbc1ff0abfe1316651fa73bbb95b023255b301b415867ff8b"
SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "20ed9ec0599e6582a218dae544566ddf7e2af46341705f35de874c90d7eecc0c"

SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "43c89f16832e16fff0b2c51b953c8295db97bc5623cb1bf0164d992a7f29af03"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "0208dd644f6e266cc7c07695889f1280e04be06672cb172a401a444b54ffa9e9"
SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "3a5a1dddf679720fec5c7af9e38f3ed5d7fe134c430458fae98f116da01becf6"

SRC_URI[cargo-snapshot-s390x.sha256sum] = "f2ce1bfc373efe162ca5b8ccfdb366dd526710fac305c61ed0b582b9185d68e9"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "9ebe880ea998fc13d3ed9cbe71d26c69dd93859be8a8e582a8ddc5393400d4e5"
SRC_URI[rustc-snapshot-s390x.sha256sum] = "41a958bc4313fb0c0446376c84476818820cfe75937675a9594823b63dc9e219"

SRC_URI[cargo-snapshot-x86_64.sha256sum] = "4a401dfe7b3056dc0d42acbcd380b2b90f936577706ca74ef5327af0f5abd0a0"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "36f27513a6e4381f15b0cd14097c885af537f990cb6193cec3337c429367bf23"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "5b5d7854a0d73368f15146c1aa47e4dbccf12762c93282f410a09a605929ce09"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "57df2bdcfb659cb34bcb199400e84eb09d564fc390e5f8d3b011a15955241266"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "b39c5d31ad61c5670a09dc4c8020f888e9b5be2dd0deec90899a7ed14b759488"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
