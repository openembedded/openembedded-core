## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.json
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.76.0"

SRC_URI[cargo-snapshot-aarch64.sha256sum] = "d0c54d824e64b7313a974409541ca3a157b3ed7299865786bd0c440b0e073091"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "4d9d8e392b03965b01591f16b233e91d48a597e731e0cd04dc495512dd4775e2"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "c0c579d9321da855109a2b6c7d7c9e01549db37e8490f058cfdc0012bef394cd"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "a299b5053d2771c24e3d5787caa0317ea729618700acacf2eeb95e345a92529c"

SRC_URI[cargo-snapshot-i686.sha256sum] = "ffa9b2b65a3bd6298ffd5a2defd6ac5fa42d1a0dcefdea50b92ddf8d162db19c"
SRC_URI[clippy-snapshot-i686.sha256sum] = "a6eb9271424ac326ef7609b1c2113916e42801ca271348708ff032490b66ac50"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "78fb0859beb4f18af7227af3f09f0218169ca38c7751865e40d78d697ca190fc"
SRC_URI[rustc-snapshot-i686.sha256sum] = "e52ff92c605284b049e88912343a0f1c9c4cef7cc4a501d4e8f1ab9e4d9b1d7e"

SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "ff5ece4942139dac43bae14293602cce5bb87c700fa051eb5a6cd7b08eebeafc"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "ed8abd72fbbd9320573752eee465559743b94832007b3beda9b7ecbee596bf61"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "0b96bc12af2d2477e7e6b22befbf37b759e56035414d8aa85975e3050c88dae5"
SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "59c8ec9441a73223873bf561f0c36719b130788a03e8e04955e3b79625a8d68c"

SRC_URI[cargo-snapshot-powerpc.sha256sum] = "b5c2e394fb300b480d9e15d519dba6dd40a20fda91117dc3ede721af1bac8645"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "1227f8989ae609afe0d771060262e113ead080ac863bd5c7f89a6b491d5b7025"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "773189b5fc9c2252801dd10da23dfd54577f25fcaf8f4077b4d5bc2169079653"
SRC_URI[rustc-snapshot-powerpc.sha256sum] = "00b27ff304d5af9663778a6f6dd70a85a98fe8db877054bb4080206372faf571"

SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "dcf7e9e0b247f56759528db601eec76ff9c471457acd84c4c47e42b35ad2edd2"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "903e30eeefd11d923412e2ddfb695126d80d5101afd1838e61d7cdca4acdad04"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "312699dea45a90f4f80bba87e12c410378b39c819bd089b1219101f101b49833"
SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "ab397f7ea75b18b5c5e01b878772f1c91f102a09b46c60dae5110318cb17c677"

SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "c3b249f7db0fe6db2e6a52ab183cdec544ccf7fa22c27cddb039e389da85b8f7"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "49ed5e82d833b91a943163183f5c4cfccae92dca9b2758a846dc9f084e0a5651"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "939fe0ffe27b4cbec4e8f43a69e795460f507b3b57753e2644546d9d57fe7a39"
SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "51ad28e1e646c6659110ecc8189fc519916e9317c50a05d20e4555e919751edb"

SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "2799a565834247d67b7e84dda209a662aaa6fd16bbb7d46d78882de3a56b7df0"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "ea678064035f634c877bd3cc2559beb82bee10b5e2cddc2e3e37baea0ce4b472"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "ecbf009619d6704f0a8384252b41d11851dadbf0d9809ec80d77765d029883a4"
SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "f0661f1ae574947c6762b8ffd756de7e9199ff4790f1fcb9c994a3b49684a9a0"

SRC_URI[cargo-snapshot-s390x.sha256sum] = "3697dab26ed0bc98f8d536d81f7176e5162dfc3629507d942b7c7d4e7b1db928"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "657521dcc91256cd023fe2c3c29d12d402f0a905dbd3713eddd5670c3fe36bb6"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "e5eedfb57fe7f93c77ea91df391607ffe553349169c1b60b5d8ad040a7fd639b"
SRC_URI[rustc-snapshot-s390x.sha256sum] = "c59a8246e5bd960b5875d8ab4e66be9d9adf376b61e99dee32a3e3e66f8197e6"

SRC_URI[cargo-snapshot-x86_64.sha256sum] = "30ec0ad9fca443ec12c544f9ce448dacdde411a45b9042961938b650e918ccfb"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "0ba679a46d2b1f4e336acfbd892f50c800ba090ddc8de37fde679f9a6241ba7b"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "e41150b52d923a3bbe166c4ecc5719f56576274b0d034d764768aee279ae2063"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "9fadfcf71bc6a0ddfd026b9624163faf1c5689dd4a1f7cc1f857167ade4aa6eb"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "d456e1b697ce63e327df44945478a276938bbd293bc564ff4ff075603e7f521d"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "9a192c3a41d15f812346f5a36c4503937cbeffc8f05856a6747f6c880dbd8729"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"
RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
