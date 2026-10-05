RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://rust-oe-selftest.patch;patchdir=${RUSTSRC} \
            file://cc-hash-object-names-relative-to-source-roots.patch;patchdir=${RUSTSRC} \
            file://0001-src-core-build_steps-tool.rs-switch-off-lto-for-rust.patch;patchdir=${RUSTSRC} \
            file://0002-Fix-rust-build-failure-with-unstable-options.patch;patchdir=${RUSTSRC} \
            file://0003-bootstrap-skip-StdarchVerify-when-remote-testing.patch;patchdir=${RUSTSRC} \
            file://0005-rustc_codegen_llvm-Do-not-pass-amx-tf32-to-LLVM-23.patch;patchdir=${RUSTSRC} \
            file://0007-cargo-omit-host-triple-from-unit-metadata-hash.patch;patchdir=${RUSTSRC} \
            file://0009-rustc-span-make-hygiene-encoding-order-deterministic.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "cc41916a8c84f5d9ec4f55561b44e39f43b647b133f8a6f51be9ea13c83f7036"

# Point UNPACKDIR into the work-shared location so rust-source.bb and all
# consumers (via S/RUSTSRC) reference a single shared source tree.
UNPACKDIR = "${TMPDIR}/work-shared/rust-source-${PV}-${PR}/sources"
RUSTSRC = "${UNPACKDIR}/rustc-${PV}-src"
S = "${RUSTSRC}"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
