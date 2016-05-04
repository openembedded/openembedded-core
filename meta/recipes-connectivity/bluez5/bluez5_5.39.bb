require bluez5.inc

REQUIRED_DISTRO_FEATURES = "bluez5"

SRC_URI[md5sum] = "c0c2b8d1151c7538bf3ae7bfc78aa912"
SRC_URI[sha256sum] = "21d1bc9150d3576296595217efb98a746b592389d25d5637e8bee5da7272593b"

# noinst programs in Makefile.tools that are conditional on READLINE
# support
NOINST_TOOLS_READLINE ?= " \
    attrib/gatttool \
    tools/obex-client-tool \
    tools/obex-server-tool \
    tools/bluetooth-player \
    tools/obexctl \
"

# noinst programs in Makefile.tools that are conditional on EXPERIMENTAL
# support
NOINST_TOOLS_EXPERIMENTAL ?= " \
    emulator/btvirt \
    emulator/b1ee \
    emulator/hfp \
    tools/3dsp \
    tools/mgmt-tester \
    tools/gap-tester \
    tools/l2cap-tester \
    tools/sco-tester \
    tools/smp-tester \
    tools/hci-tester \
    tools/rfcomm-tester \
    tools/bdaddr \
    tools/avinfo \
    tools/avtest \
    tools/scotest \
    tools/amptest \
    tools/hwdb \
    tools/hcieventmask \
    tools/hcisecfilter \
    tools/btmgmt \
    tools/btinfo \
    tools/btattach \
    tools/btsnoop \
    tools/btproxy \
    tools/btiotest \
    tools/mcaptest \
    tools/cltest \
    tools/oobtest \
    tools/seq2bseq \
    tools/ibeacon \
    tools/btgatt-client \
    tools/btgatt-server \
    tools/gatt-service \
    profiles/iap/iapd \
"
