RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://rust-oe-selftest.patch;patchdir=${RUSTSRC} \
            file://cc-hash-object-names-relative-to-source-roots.patch;patchdir=${RUSTSRC} \
            file://0001-src-core-build_steps-tool.rs-switch-off-lto-for-rust.patch;patchdir=${RUSTSRC} \
            file://0002-Fix-rust-build-failure-with-unstable-options.patch;patchdir=${RUSTSRC} \
            file://0003-cargo-omit-host-triple-from-unit-metadata-hash.patch;patchdir=${RUSTSRC} \
            file://0004-Re-bless-tests-adjust-for-new-Cargo-lints.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "90dab90b8084eb57c35ae2b1d0b70c8e5138ace6023b22c60a5a400f586f63b2"

# Point UNPACKDIR into the work-shared location so rust-source.bb and all
# consumers (via S/RUSTSRC) reference a single shared source tree.
UNPACKDIR = "${TMPDIR}/work-shared/rust-source-${PV}-${PR}/sources"
RUSTSRC = "${UNPACKDIR}/rustc-${PV}-src"
S = "${RUSTSRC}"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
