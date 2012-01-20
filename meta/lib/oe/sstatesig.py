import bb.siggen

def sstate_rundepfilter(fn, recipename, task, dep, depname):
    # Return True if we should keep the dependency, False to drop it
    def isNative(x):
        return x.endswith("-native")
    def isCross(x):
        return x.endswith("-cross") or x.endswith("-cross-initial") or x.endswith("-cross-intermediate")
    def isNativeSDK(x):
        return x.endswith("-nativesdk")

    # Always include our own inter-task dependencies
    if recipename == depname:
        return True

    # Quilt (patch application) changing isn't likely to affect anything
    if depname == "quilt-native":
        return False
    # Don't change native/cross/nativesdk recipe dependencies any further
    if isNative(recipename) or isCross(recipename) or isNativeSDK(recipename):
        return True

    # Only target packages beyond here

    # Drop native/cross/nativesdk dependencies from target recipes
    if isNative(depname) or isCross(depname) or isNativeSDK(depname):
        return False

    # Default to keep dependencies
    return True

class SignatureGeneratorOEBasic(bb.siggen.SignatureGeneratorBasic):
    name = "OEBasic"
    def init_rundepcheck(self, data):
        pass
    def rundep_check(self, fn, recipename, task, dep, depname):
        return sstate_rundepfilter(fn, recipename, task, dep, depname)

class SignatureGeneratorOEBasicHash(bb.siggen.SignatureGeneratorBasicHash):
    name = "OEBasicHash"
    def init_rundepcheck(self, data):
        pass
    def rundep_check(self, fn, recipename, task, dep, depname):
        return sstate_rundepfilter(fn, recipename, task, dep, depname)

# Insert these classes into siggen's namespace so it can see and select them
bb.siggen.SignatureGeneratorOEBasic = SignatureGeneratorOEBasic
bb.siggen.SignatureGeneratorOEBasicHash = SignatureGeneratorOEBasicHash
