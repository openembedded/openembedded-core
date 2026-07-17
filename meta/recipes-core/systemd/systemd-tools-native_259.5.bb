FILESEXTRAPATHS:prepend := "${THISDIR}/systemd:"

require systemd.inc

SUMMARY = "native tools from systemd"

# We don't actually need jinja to generate code, but it's checked for at configure time
DEPENDS = "gperf-native python3-jinja2-native"

inherit pkgconfig meson native

# Disable everything that is auto-detected by default
EXTRA_OEMESON += "--auto-features disabled"

# Link systemctl statically
EXTRA_OEMESON += "-Dlink-systemctl-shared=false"

# Ensure unused build paths are not in the binary
EXTRA_OEMESON += "-Dsysvinit-path= -Dsysvrcnd-path="

# Systemctl is supposed to operate on target, but the target sysroot is not
# determined at run-time, but rather set during configure
# More details are here https://github.com/systemd/systemd/issues/35897#issuecomment-2665405887
EXTRA_OEMESON += "--sysconfdir ${sysconfdir_native}"

MESON_TARGET = "systemctl"
MESON_INSTALL_TAGS = "systemctl"
