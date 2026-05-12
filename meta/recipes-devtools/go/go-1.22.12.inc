require go-common.inc

FILESEXTRAPATHS:prepend := "${FILE_DIRNAME}/go:"

LIC_FILES_CHKSUM = "file://LICENSE;md5=5d4950ecb7b26d2c5e4e7b4e0dd74707"

SRC_URI += "\
    file://0001-cmd-go-make-content-based-hash-generation-less-pedan.patch \
    file://0002-cmd-go-Allow-GOTOOLDIR-to-be-overridden-in-the-envir.patch \
    file://0003-ld-add-soname-to-shareable-objects.patch \
    file://0004-make.bash-override-CC-when-building-dist-and-go_boot.patch \
    file://0005-cmd-dist-separate-host-and-target-builds.patch \
    file://0006-cmd-go-make-GOROOT-precious-by-default.patch \
    file://0007-exec.go-filter-out-build-specific-paths-from-linker-.patch \
    file://0008-src-cmd-dist-buildgo.go-do-not-hardcode-host-compile.patch \
    file://0009-go-Filter-build-paths-on-staticly-linked-arches.patch \
    file://CVE-2025-22870.patch \
    file://CVE-2025-22871.patch \
    file://CVE-2025-4673.patch \
    file://CVE-2025-4674.patch \
    file://CVE-2025-47907-pre.patch \
    file://CVE-2025-47907.patch \
    file://CVE-2025-47906.patch \
    file://CVE-2025-58185.patch \
    file://CVE-2025-58187-1.patch \
    file://CVE-2025-58187-2.patch \
    file://CVE-2025-58188.patch \
    file://CVE-2025-58189.patch \
    file://CVE-2025-47912.patch \
    file://CVE-2025-61723.patch \
    file://CVE-2025-61724.patch \
    file://CVE-2025-61727.patch \
    file://CVE-2025-61729.patch \
    file://CVE-2025-61730.patch \
    file://CVE-2025-61726.patch \
    file://CVE-2025-61728.patch \
    file://CVE-2025-61731.patch \
    file://CVE-2025-68119-dependent.patch \
    file://CVE-2025-68119.patch \
    file://CVE-2025-61732.patch \
    file://CVE-2025-68121_p1.patch \
    file://CVE-2025-68121_p2.patch \
    file://CVE-2025-68121_p3.patch \
    file://CVE-2026-27140.patch \
    file://CVE-2026-27142.patch \
    file://CVE-2026-27143.patch \
    file://CVE-2026-27144.patch \
    file://CVE-2026-32280.patch \
    file://CVE-2026-32283.patch \
    file://CVE-2026-32289.patch \
    file://CVE-2026-33811.patch \
    file://CVE-2026-39817.patch \
    file://CVE-2026-39819.patch \
    file://CVE-2026-39820.patch \
    file://CVE-2026-39825.patch \
    file://CVE-2026-39826.patch \
    file://CVE-2026-42499.patch \
    file://CVE-2026-42501.patch \
    file://CVE-2026-42504.patch \
    file://CVE-2026-42507.patch \
"
SRC_URI[main.sha256sum] = "012a7e1f37f362c0918c1dfa3334458ac2da1628c4b9cf4d9ca02db986e17d71"

CVE_STATUS[CVE-2025-0913] = "not-applicable-platform: Issue only applies on Windows"
