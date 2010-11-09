# Check types of bitbake configuration variables
#
# See oe.types for details.

python check_types() {
    import oe.types
    if isinstance(e, bb.event.ConfigParsed):
        for key in e.data.keys():
            if e.data.getVarFlag(key, "type"):
                oe.types.value(key, e.data)
}
addhandler check_types
