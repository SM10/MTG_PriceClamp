package com.stephenhobby.mtgpriceclamp.datatype

import android.net.Uri

class Card(val id: String,
           val oracle_id : String,
           val tcgplayer_id : Int,
           val cardmarket_id : Int,
           val name : String) {

    var lang : String? = null
    var release_date : String? = null
    var uri : Uri? = null
    var scryfall_uri : Uri? = null
    var layout : String? = null
    var highres : Boolean? = null
    var image_status : String? = null
    var image_uris : HashMap<String, Uri>? = null
    var mana_cost : String? = null
    var cmc : Int? = null
    var type_line : String? = null
    var oracle_text : String? = null
    var power : Int? = null
    var toughness : Int? = null
    var colors : Array<Char>? = null
    var color_identity : Array<Char>? = null
    var keywords : Array<String>? = null
    var legalities : HashMap<String, String>? = null
    var reserved : Boolean? = null
    var foil : Boolean? = null
    var nonfoil : Boolean? = null
    var finishes : Array<String>? = null
    var oversized : Boolean? = null
    var promo : Boolean? = null
    var reprint : Boolean? = null
    var variation : Boolean? = null
    var set_id : String? = null
    var set_code : String? = null
    var set_name : String? = null
    var set_type : String? = null
    var set_uri : Uri? = null
    var set_search_uri : Uri? = null
    var scryfall_set_uri : Uri? = null
    var rulings_uri : Uri? = null
    var prints_search_uri : Uri? = null
    var collector_number : String? = null
    var digital : Boolean? = null
    var rarity : String? = null
    var card_back_id : String? = null
    var artist: String? = null
    var artist_ids : Array<String>? = null
    var illustration_id : String? = null
    var border_color : String? = null
    var frame : String? = null
    var full_art : Boolean? = null
    var textless : Boolean? = null
    var booster: Boolean? = null
    var story_spotlight : Boolean? = null
    var prices : HashMap<String, Double?>? = null
    var related_uris : HashMap<String, Uri?>? = null
    var purchase_uris : HashMap<String, Uri?>? = null
}