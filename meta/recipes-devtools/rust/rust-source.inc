RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://rv32-cargo-rustix-0.38.37-fix.patch;patchdir=${RUSTSRC} \
            file://rust-oe-selftest.patch;patchdir=${RUSTSRC} \
	    file://repro-issue-fix-with-cc-crate-hashmap.patch;patchdir=${RUSTSRC} \
            file://oeqa-selftest-Increase-timeout-in-process-sigpipe-ru.patch;patchdir=${RUSTSRC} \
            file://0001-src-core-build_steps-tool.rs-switch-off-lto-for-rust.patch;patchdir=${RUSTSRC} \
	    file://revert-link-std-statically-in-rustc_driver-feature.patch;patchdir=${RUSTSRC} \
	    file://revert-Zdual-proc-macros-additional-check.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "7b11d4242dab0921a7d54758ad3fe805153c979c144625fecde11735760f97df"

RUSTSRC = "${WORKDIR}/rustc-${RUST_VERSION}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"

CVE_STATUS[CVE-2024-24576] = "not-applicable-platform: Issue only applies on Windows"
CVE_STATUS[CVE-2024-43402] = "not-applicable-platform: Issue only applies on Windows"
