require glib.inc

PE = "1"

SHRT_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://run-ptest \
           file://0001-Fix-DATADIRNAME-on-uclibc-Linux.patch \
           file://Enable-more-tests-while-cross-compiling.patch \
           file://0001-Remove-the-warning-about-deprecated-paths-in-schemas.patch \
           file://0001-Install-gio-querymodules-as-libexec_PROGRAM.patch \
           file://0001-Do-not-ignore-return-value-of-write.patch \
           file://0010-Do-not-hardcode-python-path-into-various-tools.patch \
           file://0001-Set-host_machine-correctly-when-building-with-mingw3.patch \
           file://0001-Do-not-write-bindir-into-pkg-config-files.patch \
           file://0001-meson-Run-atomics-test-on-clang-as-well.patch \
           file://0001-gio-tests-resources.c-comment-out-a-build-host-only-.patch \
           file://0001-gio-tests-g-file-info-don-t-assume-million-in-one-ev.patch \
           file://CVE-2023-32665-0001.patch \
           file://CVE-2023-32665-0002.patch \
           file://CVE-2023-32665-0003.patch \
           file://CVE-2023-32665-0004.patch \
           file://CVE-2023-32665-0005.patch \
           file://CVE-2023-32665-0006.patch \
           file://CVE-2023-32665-0007.patch \
           file://CVE-2023-32665-0008.patch \
           file://CVE-2023-32665-0009.patch \
           file://CVE-2023-29499.patch \
           file://CVE-2023-32611-0001.patch \
           file://CVE-2023-32611-0002.patch \
           file://CVE-2023-32643.patch \
           file://CVE-2023-32636.patch \
           file://CVE-2024-34397_01.patch \
           file://CVE-2024-34397_02.patch \
           file://CVE-2024-34397_03.patch \
           file://CVE-2024-34397_04.patch \
           file://CVE-2024-34397_05.patch \
           file://CVE-2024-34397_06.patch \
           file://CVE-2024-34397_07.patch \
           file://CVE-2024-34397_08.patch \
           file://CVE-2024-34397_09.patch \
           file://CVE-2024-34397_10.patch \
           file://CVE-2024-34397_11.patch \
           file://CVE-2024-34397_12.patch \
           file://CVE-2024-34397_13.patch \
           file://CVE-2024-34397_14.patch \
           file://CVE-2024-34397_15.patch \
           file://CVE-2024-34397_16.patch \
           file://CVE-2024-34397_17.patch \
           file://CVE-2024-34397_18.patch \
           file://0001-gvariant-serialiser-Convert-endianness-of-offsets.patch \
           file://CVE-2024-52533.patch \
           file://gdatetime-test-fail-0001.patch \
           file://gdatetime-test-fail-0002.patch \
           file://gdatetime-test-fail-0003.patch \
           file://CVE-2025-3360-01.patch \
           file://CVE-2025-3360-02.patch \
           file://CVE-2025-3360-03.patch \
           file://CVE-2025-3360-04.patch \
           file://CVE-2025-3360-05.patch \
           file://CVE-2025-3360-06.patch \
           "
SRC_URI:append:class-native = " file://relocate-modules.patch"

SRC_URI[sha256sum] = "4a39a2f624b8512d500d5840173eda7fa85f51c109052eae806acece85d345f0"

# Find any meson cross files in FILESPATH that are relevant for the current
# build (using siteinfo) and add them to EXTRA_OEMESON.
inherit siteinfo
def find_meson_cross_files(d):
    if bb.data.inherits_class('native', d):
        return ""

    thisdir = os.path.normpath(d.getVar("THISDIR"))
    import collections
    sitedata = siteinfo_data(d)
    # filename -> found
    files = collections.OrderedDict()
    for path in d.getVar("FILESPATH").split(":"):
        for element in sitedata:
            filename = os.path.normpath(os.path.join(path, "meson.cross.d", element))
            sanitized_path = filename.replace(thisdir, "${THISDIR}")
            if sanitized_path == filename:
                if os.path.exists(filename):
                    bb.error("Cannot add '%s' to --cross-file, because it's not relative to THISDIR '%s' and sstate signature would contain this full path" % (filename, thisdir))
                continue
            files[filename.replace(thisdir, "${THISDIR}")] = os.path.exists(filename)

    items = ["--cross-file=" + k for k,v in files.items() if v]
    d.appendVar("EXTRA_OEMESON", " " + " ".join(items))
    items = ["%s:%s" % (k, "True" if v else "False") for k,v in files.items()]
    d.appendVarFlag("do_configure", "file-checksums", " " + " ".join(items))

python () {
    find_meson_cross_files(d)
}
