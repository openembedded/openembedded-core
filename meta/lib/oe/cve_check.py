import collections
import re
import itertools

_Version = collections.namedtuple(
    "_Version", ["release", "pre_l", "pre_v"]
)

class Version():
    _version_pattern =  r"""v?(?:(?P<release>[0-9]+(?:[-\.][0-9]+)*)(?P<pre>[-_\.]?(?P<pre_l>(rc|alpha|beta|pre|preview|dev))[-_\.]?(?P<pre_v>[0-9]+)?)?)(.*)?"""
    _regex = re.compile(r"^\s*" + _version_pattern + r"\s*$", re.VERBOSE | re.IGNORECASE)
    def __init__(self, version):
        match = self._regex.search(version)
        if not match:
            raise Exception("Invalid version: '{0}'".format(version))

        self._version = _Version(
            release=tuple(int(i) for i in match.group("release").replace("-",".").split(".")),
            pre_l=match.group("pre_l"),
            pre_v=match.group("pre_v")
        )

        self._key = _cmpkey(
            self._version.release,
            self._version.pre_l,
            self._version.pre_v
        )

    def __le__(self, other):
        if not isinstance(other, Version):
            return NotImplemented
        return self._key <= other._key

    def __lt__(self, other):
        if not isinstance(other, Version):
            return NotImplemented
        return self._key < other._key

    def __ge__(self, other):
        if not isinstance(other, Version):
            return NotImplemented
        return self._key >= other._key

    def __gt__(self, other):
        if not isinstance(other, Version):
            return NotImplemented
        return self._key > other._key

def _cmpkey(release, pre_l, pre_v):
    # remove leading 0
    _release = tuple(
        reversed(list(itertools.dropwhile(lambda x: x == 0, reversed(release))))
    )
    if pre_l is None and pre_v is None:
        _pre = float('inf')
    else:
        _pre = float(pre_v) if pre_v else float('-inf')
    return _release, _pre
