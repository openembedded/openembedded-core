## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.json
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.75.0"

SRC_URI[cargo-snapshot-aarch64.sha256sum] = "cf367bccbc97ba86b4cf8a0141c9c270523e38f865dc7220b3cfdd79b67200ed"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "2ea0dc380ac1fced245bafadafd0da50167a4a416b6011e3d73ba3e657a71d15"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "b1d7bb8b0420b71585cf9c4eb5fd1e986fd83edc2d393510b54a9b20272386a3"

SRC_URI[cargo-snapshot-i686.sha256sum] = "ab652e4755def10f37c245245643d60f56cb28a7dcd2defb8a1d560eafbf08f1"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "42c0aba08ddcad191c84adcb5b6479b4599b9e4656e4e2b6d3ed5b46fa4a04fb"
SRC_URI[rustc-snapshot-i686.sha256sum] = "a39aed533c63b4da6c0d9f30e76d7290ed5b3be2e66bde43d02764d7b50d382f"

SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "e4e4b38375a2fa0697b80ef6d7b0f8a4505836baded47b755ad751fbe605354b"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "fede876247ca3977a001cfca9038851eb99dd38063fc2bec439e75e25453242d"
SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "36a6507eb6c5fc6d3884579e908ab7865436a1705e42677bcb5d1d75e5bee8d7"

SRC_URI[cargo-snapshot-powerpc.sha256sum] = "c758265f0fe5a5c36fccd9f67a297c3274a795e67530aa8c09d376840d6184af"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "eb56d591650773c7ce00f32fde1adcb878b4010167f348ea167d492fba8628d0"
SRC_URI[rustc-snapshot-powerpc.sha256sum] = "49a6adcc8d26a38a77048b13b46f83581ee33262627099ea4f6d0d8eacaf889c"

SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "409c9f596e9f88da78587edff1633c319ac0ac14ea622f26d30d16ec1f92e1c9"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "776ea8d200f65cacdb619eecae319511ec2ab398321c8d976c846b9dc44b87d5"
SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "6888a22227037aa3015a2195905acdea420e51f0909cf6653b30765938ea5745"

SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "6b293abc4cafba101c714b37dc369cd229e04151efad626896b811165019bb95"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "22286818e4f0f13f712082e7a740aceb32e28ab3f568d6ed0e9e06b4972b30f5"
SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "5268dd59f78f946d2dba8ad362ba8c724432422c6f497a69f6dbad240e4497d0"

SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "13937d19fc5fa531c1def3a526695be94aeb34e7f8653fc6e5acd782bcc6267d"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "35db7870fbe27308c2a9bad0371cd05af2fd2975e00b7f5a5bb305798960a9cb"
SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "4dbed202d8d11eb1fb29204bf9d8358e8da85791e990ec1dd576f75701b33abb"

SRC_URI[cargo-snapshot-s390x.sha256sum] = "720cabf7e03d91bf080cb15d5ebb8fb56eae1938b7ab763f82aa46a997d1ddb7"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "810be45b498c4ce1ba9181702a2534c5a35e52fbab547d94b62f70e28388748d"
SRC_URI[rustc-snapshot-s390x.sha256sum] = "ce4faea55ee9a5032ad6eca5f1b0be3d572d6fc0146366c0c984831322b6e3a3"

SRC_URI[cargo-snapshot-x86_64.sha256sum] = "6ac164e7da969a1d524f747f22792e9aa08bc7446f058314445a4f3c1d31a6bd"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "136b132199f7bbda2aa0bbff6d1e6ae7d5fca2994a2f2a432a5e99de224b6314"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "2824ba4045acdddfa436da4f0bb72807b64a089aa2e7c9a66ca1a3a571114ce7"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "7bdc6cd915a47e116ca78e07fe8d5d8acb6217e2aa6187806f782d81853a39b3"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "6943fda4f3958f28883cc5fb9f455611cc42f4d228841984b8a2a0123e7b65a7"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"
RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
