## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.90.0"

SRC_URI[rustc-snapshot-aarch64.sha256sum] = "4e1a9987a11d7d91f0d5afbf5333feb62f44172e4a31f33ce7246549003217f2"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "4952abb7d9d3ed7cea4f7ea44dcb23dc67631fae4ac44a5f059b90a4b5e9223f"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "bd8d1da6fe88ea7e29338f24277c22156267447adbfc47d690467ad32d02c2a7"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "1a7f969c5712c17d960d6fba8235b33f264a9484b369dc388766a540f1ba464f"

SRC_URI[rustc-snapshot-i686.sha256sum] = "3b33cef77f9a57d592beb5117d933134bc76258006e6d4f9aed2969d20c75742"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "dc4bffa768326e0cc47d6b129bcb891af6e7b7bf09ec92bca45e49420b9837bd"
SRC_URI[cargo-snapshot-i686.sha256sum] = "c1e9b24bbfa95550b5dba9de2045a4c906bea108625781758692b2a407428391"
SRC_URI[clippy-snapshot-i686.sha256sum] = "3e902971ef6b44ccb804af73aba5dc8c6199ed09a76f178b0eed6f53f7f56eab"

SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "aca7ee7f0d98e34dafa367cef9c35626d37cf8334f356498aa2bda2fc02206b3"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "cb779575cec151223c3c144bc9ca957530fa510e36b32bc9f35bbb1b7dde9e2f"
SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "6ae37dc8eb3ff4655053a39d5f973613a82a8a8e2fff42c423ca33d8cf19e6c1"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "847b3d4bd3d2be23bafbb90cd2ed5e1bf1aacb2e7152e7d2f99bee1078aa0eaa"

SRC_URI[rustc-snapshot-powerpc.sha256sum] = "c5d898ca92063d634011525557f2acabc65d4ea268022e7005f99b6ba917357b"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "756d1bbd66ebb0d4d575b650a8a94d52546f6892bd2f864410411f6027ed879f"
SRC_URI[cargo-snapshot-powerpc.sha256sum] = "5b160a2bf0e6f470b83c5988db5b6f02b6d65d3d97a9b965acbfe940b4143b34"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "42ac7480dcac12a843803f5ee398d2a42389359178774cab20c66eda4f6927a5"

SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "7adbc3c317a73b7d504e171d65a2b0e93953e3e572dab99acabe91140101f81d"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "16daac5dc6994c03ab3b8c4e6e5e52c41f5fc5846acea08f4fdf0759cc273aa2"
SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "fdf90d22476c88d5b9cf8a3ac0937d0b9c6b7dafbf6b3afde369c2f5b8faec88"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "071e06a50d5b2975905bb2d6211f2d2372ab0dbd50b7b0097168cb64e77a4bb0"

SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "e87b8eb926a65211a99f6712ff376c5950b4b11c67ed7f92019da27a34ae7085"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "eac29f92ccd335c51553c362c7a6d3de2eb7071b5ae3839470e351a6a3ebdb77"
SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "f029151dfeed6570b8b347e04a5bb7dcb9c59d8e5454c535c05bb5069c216354"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "12d19dbec34c5a35cd285fe74dcd0226563c0b25be5d041479744060e9374c98"

SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "11ae522969c6b18ad4b5a1ae229473de59406af7ab2e831acb43ee247a16b58d"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "675dc5556d9d879101d2401b693fcf79edac8f00a49249e2e0f19cecd172ce51"
SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "8a09de014f7cec749b338c6636d190a1b73fd377bab6a223eb715d3c76c49e79"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "02338e25b3d2b61d9dbee7bee4c9ed148f18e2d010c6297ffd36df98042f41a2"

SRC_URI[rustc-snapshot-s390x.sha256sum] = "274daef5a69c8812411248a4afea4f58a096c978dffba6b1198da8e3b5a02de7"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "e3e7e3dbc65616eebc69e642d9042363ec38d7f50393ef816cc5238ec475f3f5"
SRC_URI[cargo-snapshot-s390x.sha256sum] = "468ace270ee4edac0a10185ea876ea555e41bacc0137ff08985d2155fe8cc777"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "bb0a9483a36fdbdab08b433613b374c036779d247a8d508e31265193c3759a8d"

SRC_URI[rustc-snapshot-x86_64.sha256sum] = "48c2a42de9e92fcae8c24568f5fe40d5734696a6f80e83cc6d46eef1a78f13c9"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "663f4ab7945b392d5e5294dec1b050a66820a20e86f084ec37eeb0f2f7ff5569"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "9853db03d68578a30972e2755c89c66aec035fec641cf8f3a7117c81eec2578d"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "5b6466419693a05365827378145014a37ae74fb2948fab390d5210a524792ed8"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "46eab5c41bf9160a31a53560ff5fc43308c0998cde952533e76fadef66799553"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "c204ecb424090cc6c0100cef3013f8636d29f288db99c037578aee7d2672a711"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"

RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
