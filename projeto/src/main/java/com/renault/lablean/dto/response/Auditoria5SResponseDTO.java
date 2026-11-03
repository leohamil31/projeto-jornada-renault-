package com.renault.lablean.dto.response;

public class Auditoria5SResponseDTO {
    
    private Long idAuditoria;
    private Long idApontamento;
    private Integer notaSeiri;
    private Integer notaSeiton;
    private Integer notaSeiso;
    private Integer notaSeiketsu;
    private Integer notaShitsuke;
    private Double notaTotal; 
    private String planoAcao;

    // Construtor atualizado recebendo os 9 parâmetros exatamente como o Service está enviando
    public Auditoria5SResponseDTO(Long idAuditoria, Long idApontamento, Integer notaSeiri, 
                                  Integer notaSeiton, Integer notaSeiso, Integer notaSeiketsu, 
                                  Integer notaShitsuke, Double notaTotal, String planoAcao) {
        this.idAuditoria = idAuditoria;
        this.idApontamento = idApontamento;
        this.notaSeiri = notaSeiri;
        this.notaSeiton = notaSeiton;
        this.notaSeiso = notaSeiso;
        this.notaSeiketsu = notaSeiketsu;
        this.notaShitsuke = notaShitsuke;
        this.notaTotal = notaTotal;
        this.planoAcao = planoAcao;
    }

    public Long getIdAuditoria() { return idAuditoria; }
    public Long getIdApontamento() { return idApontamento; }
    public Integer getNotaSeiri() { return notaSeiri; }
    public Integer getNotaSeiton() { return notaSeiton; }
    public Integer getNotaSeiso() { return notaSeiso; }
    public Integer getNotaSeiketsu() { return notaSeiketsu; }
    public Integer getNotaShitsuke() { return notaShitsuke; }
    public Double getNotaTotal() { return notaTotal; }
    public String getPlanoAcao() { return planoAcao; }
}