## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.txt
## TODO: find a way to add additional SRC_URIs based on the contents of an
##       earlier SRC_URI.
RS_VERSION = "1.54.0"
CARGO_VERSION = "1.54.0"

# TODO: Add hashes for other architecture toolchains as well. Make a script?
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "a22e19859b3c5032daac1633637546a986c51bdfa3ad0db6e394e6cc461f466b"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "eafc06aba7128ee8592c16d7e3ae7b877e82c5d9779c9d07099558ed7e286296"
SRC_URI[cargo-snapshot-x86_64.sha256sum] = "8a9c80b7dc5d47e6eafe173b81ad96244522c97d74214ba7da57a502d49b231c"

SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "a1a77737d218087eb7d3613adcde2c9e383d758cdf128646a0ee63396145270b"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "0e771c06435b6fa3087ce054c7f2f94b8ee36717997f32f97e2e3425739ac2b4"
SRC_URI[cargo-snapshot-aarch64.sha256sum] = "b3b6e5358bf1ecd5d8d7e258a5632dac7d923b2aa8ab1c8c0f6aad62d9307b37"

SRC_URI += " \
    https://static.rust-lang.org/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${BUILD_ARCH};subdir=rust-snapshot-components \
    https://static.rust-lang.org/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${BUILD_ARCH};subdir=rust-snapshot-components \
    https://static.rust-lang.org/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_STD_SNAPSHOT = "rust-std-${RS_VERSION}-${BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${RS_VERSION}-${BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${CARGO_VERSION}-${BUILD_ARCH}-unknown-linux-gnu"
