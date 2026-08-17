RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://0001-Define-more-ioctl-codes-on-riscv32gc-unknown-linux-g.patch;patchdir=${RUSTSRC} \
            file://rust-oe-selftest.patch;patchdir=${RUSTSRC} \
            file://repro-issue-fix-with-cc-crate-hashmap.patch;patchdir=${RUSTSRC} \
            file://0001-src-core-build_steps-tool.rs-switch-off-lto-for-rust.patch;patchdir=${RUSTSRC} \
            file://0002-Fix-rust-build-failure-with-unstable-options.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "0ed06fdaffd4722a7702e0b4eebfafc897ab8f513e8e1b247cdd7e5c6df6ded2"

# Point UNPACKDIR into the work-shared location so rust-source.bb and all
# consumers (via S/RUSTSRC) reference a single shared source tree.
UNPACKDIR = "${TMPDIR}/work-shared/rust-source-${PV}-${PR}/sources"
RUSTSRC = "${UNPACKDIR}/rustc-${PV}-src"
S = "${RUSTSRC}"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
