/**
 * 矢印キーで選択をできるようにする
 */
document.onkeydown = function(e){
    if(e.keyCode === 38) { // ↑
    	focus_prev();
        return false;
    }
    if(e.keyCode === 40) { // ↓
        focus_next();
        return false;
    }
};

/**
 * フォーカスを前に移動する
 */
function focus_prev() {
    // 現在のフォーカスを取得
    var currentFocusIndex = $('a').index($(':focus'));

    if(currentFocusIndex > -1) {
        for (var i = 0; i < $('a').length; i++) {
            if(i === currentFocusIndex && i > 0) {
                $('a').eq(i - 1).focus();
            }
        }
    }
}

/**
 * フォーカスを次に移動する
 */
function focus_next() {
    // 現在のフォーカスを取得
    var currentFocusIndex = $('a').index($(':focus'));

    if(currentFocusIndex > -1) {
        for (var i = 0; i < $('a').length; i++) {
            if(i === currentFocusIndex && i < $('a').length - 1) {
                $('a').eq(i + 1).focus();
            }
        }
    } else {
        $('a').eq(0).focus();
    }
}
