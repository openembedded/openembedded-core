RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://0001-Define-more-ioctl-codes-on-riscv32gc-unknown-linux-g.patch;patchdir=${RUSTSRC} \
            file://rust-oe-selftest.patch;patchdir=${RUSTSRC} \
            file://repro-issue-fix-with-cc-crate-hashmap.patch;patchdir=${RUSTSRC} \
            file://oeqa-selftest-Increase-timeout-in-process-sigpipe-ru.patch;patchdir=${RUSTSRC} \
            file://0001-src-core-build_steps-tool.rs-switch-off-lto-for-rust.patch;patchdir=${RUSTSRC} \
            file://revert-link-std-statically-in-rustc_driver-feature.patch;patchdir=${RUSTSRC} \
            file://0001-riscv32-Define-plain-syscalls-as-their-time64-varian.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "e30d898272c587a22f77679f03c5e8192b5645c7c9ccc3407ad1106761507cea"

RUSTSRC = "${UNPACKDIR}/rustc-${RUST_VERSION}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
